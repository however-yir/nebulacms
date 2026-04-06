package io.nebulacms.app.core.endpoint.console;

import static org.springdoc.core.fn.builders.apiresponse.Builder.responseBuilder;
import static org.springdoc.core.fn.builders.content.Builder.contentBuilder;
import static org.springdoc.core.fn.builders.parameter.Builder.parameterBuilder;
import static org.springdoc.core.fn.builders.requestbody.Builder.requestBodyBuilder;

import io.swagger.v3.oas.annotations.enums.ParameterIn;
import java.time.Instant;
import org.springdoc.core.fn.builders.schema.Builder;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import io.nebulacms.app.content.comment.CommentQuery;
import io.nebulacms.app.content.comment.CommentRequest;
import io.nebulacms.app.content.comment.CommentService;
import io.nebulacms.app.content.comment.ListedComment;
import io.nebulacms.app.content.comment.ReplyRequest;
import io.nebulacms.app.content.comment.ReplyService;
import io.nebulacms.app.core.extension.content.Comment;
import io.nebulacms.app.core.extension.content.Reply;
import io.nebulacms.app.core.extension.endpoint.CustomEndpoint;
import io.nebulacms.app.extension.ListResult;
import io.nebulacms.app.infra.utils.HaloUtils;
import io.nebulacms.app.infra.utils.IpAddressUtils;


/**
 * Endpoint for managing comment.
 *
 * @author guqing
 * @since 2.0.0
 */
@Component
public class CommentEndpoint implements CustomEndpoint {

    private final CommentService commentService;
    private final ReplyService replyService;

    public CommentEndpoint(CommentService commentService, ReplyService replyService) {
        this.commentService = commentService;
        this.replyService = replyService;
    }

    @Override
    public RouterFunction<ServerResponse> endpoint() {
        final var tag = "CommentV1alpha1Console";
        return SpringdocRouteBuilder.route()
            .GET("comments", this::listComments, builder -> {
                    builder.operationId("ListComments")
                        .description("List comments.")
                        .tag(tag)
                        .response(responseBuilder()
                            .implementation(ListResult.generateGenericClass(ListedComment.class))
                        );
                    CommentQuery.buildParameters(builder);
                }
            )
            .POST("comments", this::createComment,
                builder -> builder.operationId("CreateComment")
                    .description("Create a comment.")
                    .tag(tag)
                    .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                            .mediaType(MediaType.APPLICATION_JSON_VALUE)
                            .schema(Builder.schemaBuilder()
                                .implementation(CommentRequest.class))
                        ))
                    .response(responseBuilder()
                        .implementation(Comment.class))
            )
            .POST("comments/{name}/reply", this::createReply,
                builder -> builder.operationId("CreateReply")
                    .description("Create a reply.")
                    .tag(tag)
                    .parameter(parameterBuilder().name("name")
                        .in(ParameterIn.PATH)
                        .required(true)
                        .implementation(String.class))
                    .requestBody(requestBodyBuilder()
                        .required(true)
                        .content(contentBuilder()
                            .mediaType(MediaType.APPLICATION_JSON_VALUE)
                            .schema(Builder.schemaBuilder()
                                .implementation(ReplyRequest.class))
                        ))
                    .response(responseBuilder()
                        .implementation(Reply.class))
            )
            .build();
    }

    Mono<ServerResponse> listComments(ServerRequest request) {
        CommentQuery commentQuery = new CommentQuery(request);
        return commentService.listComment(commentQuery)
            .flatMap(listedComments -> ServerResponse.ok().bodyValue(listedComments));
    }

    Mono<ServerResponse> createComment(ServerRequest request) {
        return request.bodyToMono(CommentRequest.class)
            .flatMap(commentRequest -> {
                Comment comment = commentRequest.toComment();
                comment.getSpec().setIpAddress(IpAddressUtils.getIpAddress(request));
                comment.getSpec().setUserAgent(HaloUtils.userAgentFrom(request));
                return commentService.create(comment);
            })
            .flatMap(comment -> ServerResponse.ok().bodyValue(comment));
    }

    Mono<ServerResponse> createReply(ServerRequest request) {
        String commentName = request.pathVariable("name");
        return request.bodyToMono(ReplyRequest.class)
            .flatMap(replyRequest -> {
                Reply reply = replyRequest.toReply();
                // Create via console without audit
                reply.getSpec().setApproved(true);
                reply.getSpec().setApprovedTime(Instant.now());
                reply.getSpec().setIpAddress(IpAddressUtils.getIpAddress(request));
                reply.getSpec().setUserAgent(HaloUtils.userAgentFrom(request));
                // fix gh-2951
                if (reply.getSpec().getHidden() == null) {
                    reply.getSpec().setHidden(false);
                }
                return replyService.create(commentName, reply);
            })
            .flatMap(comment -> ServerResponse.ok().bodyValue(comment));
    }

}

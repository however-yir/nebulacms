package io.nebulacms.app.content.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import io.nebulacms.app.core.extension.content.Reply;
import io.nebulacms.app.extension.Extension;
import io.nebulacms.app.extension.ExtensionStoreUtil;
import io.nebulacms.app.extension.PageRequestImpl;
import io.nebulacms.app.extension.ReactiveExtensionClient;
import io.nebulacms.app.extension.SchemeManager;
import io.nebulacms.app.extension.store.ReactiveExtensionStoreClient;
import io.nebulacms.app.infra.utils.JsonUtils;

/**
 * Integration tests for {@link ReplyServiceImpl}.
 *
 * @author guqing
 * @since 2.15.0
 */
class ReplyServiceImplIntegrationTest {

    @Nested
    @DirtiesContext
    @SpringBootTest
    class ReplyRemoveTest {
        private final List<Reply> storedReplies = createReplies(320);

        private List<Reply> createReplies(int size) {
            List<Reply> replies = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                var reply = JsonUtils.jsonToObject(fakeReplyJson(), Reply.class);
                reply.getMetadata().setName("reply-" + i);
                replies.add(reply);
            }
            return replies;
        }

        @Autowired
        private SchemeManager schemeManager;

        @MockitoSpyBean
        private ReactiveExtensionClient reactiveClient;

        @Autowired
        private ReactiveExtensionStoreClient storeClient;

        @MockitoSpyBean
        private ReplyServiceImpl replyService;

        Mono<Extension> deleteImmediately(Extension extension) {
            var name = extension.getMetadata().getName();
            var scheme = schemeManager.get(extension.getClass());

            // delete from db
            var storeName = ExtensionStoreUtil.buildStoreName(scheme, name);
            return storeClient.delete(storeName, extension.getMetadata().getVersion())
                .thenReturn(extension);
        }

        @BeforeEach
        void setUp() {
            Flux.fromIterable(storedReplies)
                .flatMap(post -> reactiveClient.create(post))
                .as(StepVerifier::create)
                .expectNextCount(storedReplies.size())
                .verifyComplete();
        }

        @AfterEach
        void tearDown() {
            Flux.fromIterable(storedReplies)
                .flatMap(this::deleteImmediately)
                .as(StepVerifier::create)
                .expectNextCount(storedReplies.size())
                .verifyComplete();
        }

        @Test
        void removeAllByComment() {
            String commentName = "fake-comment";
            replyService.removeAllByComment(commentName)
                .as(StepVerifier::create)
                .verifyComplete();

            verify(reactiveClient, times(storedReplies.size())).delete(any(Reply.class));
            verify(replyService, times(2)).listRepliesByComment(eq(commentName), any());

            replyService.listRepliesByComment(commentName, PageRequestImpl.ofSize(1))
                .as(StepVerifier::create)
                .consumeNextWith(result -> assertThat(result.getTotal()).isEqualTo(0))
                .verifyComplete();
        }
    }

    String fakeReplyJson() {
        return """
                {
                    "metadata":{
                        "name":"fake-reply"
                    },
                    "spec":{
                        "raw":"fake-raw",
                        "content":"fake-content",
                        "owner":{
                            "kind":"User",
                            "name":"fake-user",
                            "displayName":"fake-display-name"
                        },
                        "creationTime": "2024-03-11T06:23:42.923294424Z",
                        "ipAddress":"",
                        "approved": true,
                        "hidden": false,
                        "allowNotification": false,
                        "top": false,
                        "priority": 0,
                        "commentName":"fake-comment"
                    },
                    "owner":{
                        "kind":"User",
                        "displayName":"fake-display-name"
                    },
                    "stats":{
                        "upvote":0
                    }
                }
            """;
    }
}
package io.nebulacms.app.core.extension;

import lombok.Data;
import lombok.EqualsAndHashCode;
import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;
import io.nebulacms.app.extension.Metadata;

/**
 * A counter for number of requests by extension resource name.
 *
 * @author guqing
 * @since 2.0.0
 */
@Data
@GVK(group = "metrics.nebulacms.io", version = "v1alpha1", kind = "Counter", plural = "counters",
    singular = "counter")
@EqualsAndHashCode(callSuper = true)
public class Counter extends AbstractExtension {

    private Integer visit;

    private Integer upvote;

    private Integer downvote;

    private Integer totalComment;

    private Integer approvedComment;

    public static Counter emptyCounter(String name) {
        Counter counter = new Counter();
        counter.setMetadata(new Metadata());
        counter.getMetadata().setName(name);
        counter.setUpvote(0);
        counter.setTotalComment(0);
        counter.setApprovedComment(0);
        counter.setVisit(0);
        return counter;
    }
}

package io.nebulacms.app.notification.endpoint;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.net.MalformedURLException;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import io.nebulacms.app.core.extension.notification.Subscription;
import io.nebulacms.app.extension.Metadata;
import io.nebulacms.app.infra.ExternalUrlSupplier;

/**
 * Tests for {@link SubscriptionRouter}.
 *
 * @author guqing
 * @since 2.9.0
 */
@ExtendWith(MockitoExtension.class)
class SubscriptionRouterTest {

    @Mock
    private ExternalUrlSupplier externalUrlSupplier;

    @InjectMocks
    SubscriptionRouter subscriptionRouter;

    @Test
    void getUnsubscribeUrlTest() throws MalformedURLException {
        when(externalUrlSupplier.getRaw()).thenReturn(URI.create("https://nebulacms.io").toURL());
        var subscription = new Subscription();
        subscription.setMetadata(new Metadata());
        subscription.getMetadata().setName("fake-subscription");
        subscription.setSpec(new Subscription.Spec());
        subscription.getSpec().setUnsubscribeToken("fake-unsubscribe-token");

        var url = subscriptionRouter.getUnsubscribeUrl(subscription);
        assertThat(url).isEqualTo("https://nebulacms.io/apis/api.notification.nebulacms.io/v1alpha1"
            + "/subscriptions/fake-subscription/unsubscribe"
            + "?token=fake-unsubscribe-token");
    }
}

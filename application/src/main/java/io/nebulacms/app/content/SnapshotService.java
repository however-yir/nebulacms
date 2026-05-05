package io.nebulacms.app.content;

import io.nebulacms.app.core.extension.content.Snapshot;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import reactor.core.publisher.Mono;

public interface SnapshotService {

    Mono<Snapshot> getBy(String snapshotName);

    Mono<Snapshot> getPatchedBy(String snapshotName, String baseSnapshotName);

    Mono<Snapshot> patchAndCreate(@NonNull Snapshot snapshot,
        @Nullable Snapshot baseSnapshot,
        @NonNull Content content);

    Mono<Snapshot> patchAndUpdate(@NonNull Snapshot snapshot,
        @NonNull Snapshot baseSnapshot,
        @NonNull Content content);

}

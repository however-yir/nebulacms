package io.nebulacms.app.migration;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import io.nebulacms.app.extension.AbstractExtension;
import io.nebulacms.app.extension.GVK;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@GVK(group = "migration.nebulacms.io", version = "v1alpha1", kind = "Backup",
    plural = "backups", singular = "backup")
public class Backup extends AbstractExtension {

    private Spec spec = new Spec();

    private Status status = new Status();

    @Data
    @Schema(name = "BackupSpec")
    public static class Spec {

        @Schema(description = "Backup file format. Currently, only zip format is supported.")
        private String format;

        private Instant expiresAt;

    }

    @Data
    @Schema(name = "BackupStatus")
    public static class Status {

        private Phase phase = Phase.PENDING;

        private Instant startTimestamp;

        private Instant completionTimestamp;

        private String failureReason;

        private String failureMessage;

        /**
         * Size of backup file. Data unit: byte
         */
        private Long size;

        /**
         * Name of backup file.
         */
        private String filename;
    }

    public enum Phase {
        PENDING,
        RUNNING,
        SUCCEEDED,
        FAILED,
    }

}

package io.nebulacms.app.infra.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static io.nebulacms.app.infra.utils.FileNameUtils.randomFileName;
import static io.nebulacms.app.infra.utils.FileNameUtils.removeFileExtension;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

class FileNameUtilsTest {

    @Nested
    class RemoveFileExtensionTest {

        @Test
        public void shouldNotRemoveExtIfNoExt() {
            assertEquals("halo", removeFileExtension("halo", true));
            assertEquals("halo", removeFileExtension("halo", false));
        }

        @Test
        public void shouldRemoveExtIfHasOnlyOneExt() {
            assertEquals("nebulacms", removeFileExtension("nebulacms.io", true));
            assertEquals("nebulacms", removeFileExtension("nebulacms.io", false));
        }

        @Test
        public void shouldNotRemoveExtIfDotfile() {
            assertEquals(".halo", removeFileExtension(".halo", true));
            assertEquals(".halo", removeFileExtension(".halo", false));
        }

        @Test
        public void shouldRemoveExtIfDotfileHasOneExt() {
            assertEquals(".nebulacms", removeFileExtension(".nebulacms.io", true));
            assertEquals(".nebulacms", removeFileExtension(".nebulacms.io", false));
        }

        @Test
        public void shouldRemoveExtIfHasTwoExt() {
            assertEquals("halo", removeFileExtension("halo.tar.gz", true));
            assertEquals("halo.tar", removeFileExtension("halo.tar.gz", false));
        }

        @Test
        public void shouldRemoveExtIfDotfileHasTwoExt() {
            assertEquals(".halo", removeFileExtension(".halo.tar.gz", true));
            assertEquals(".halo.tar", removeFileExtension(".halo.tar.gz", false));
        }

        @Test
        void shouldReturnNullIfFilenameIsNull() {
            assertNull(removeFileExtension(null, true));
            assertNull(removeFileExtension(null, false));
        }
    }

    @Nested
    class AppendRandomFileNameTest {
        @Test
        void normalFileName() {
            String randomFileName = randomFileName("nebulacms.io", 3);
            assertEquals(16, randomFileName.length());
            assertTrue(randomFileName.startsWith("nebulacms-"));
            assertTrue(randomFileName.endsWith(".io"));

            randomFileName = randomFileName(".run", 3);
            assertEquals(7, randomFileName.length());
            assertTrue(randomFileName.endsWith(".run"));

            randomFileName = randomFileName("halo", 3);
            assertEquals(8, randomFileName.length());
            assertTrue(randomFileName.startsWith("halo-"));
        }
    }
}

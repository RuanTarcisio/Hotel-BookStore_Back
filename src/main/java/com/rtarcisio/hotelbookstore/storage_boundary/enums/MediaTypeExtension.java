package com.rtarcisio.hotelbookstore.storage_boundary.enums;

import lombok.Getter;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
public enum MediaTypeExtension {

    // Images
    PNG(MediaType.IMAGE_PNG, "png"),
    GIF(MediaType.IMAGE_GIF, "gif"),
    JPEG(MediaType.IMAGE_JPEG, "jpg", "jpeg"),
    WEBP(MediaType.valueOf("image/webp"), "webp"),
    SVG(MediaType.valueOf("image/svg+xml"), "svg"),
    BMP(MediaType.valueOf("image/bmp"), "bmp"),
    ICO(MediaType.valueOf("image/x-icon"), "ico"),

    // Documents
    PDF(MediaType.APPLICATION_PDF, "pdf"),
    DOC(MediaType.valueOf("application/msword"), "doc"),
    DOCX(MediaType.valueOf("application/vnd.openxmlformats-officedocument.wordprocessingml.document"), "docx"),
    XLS(MediaType.valueOf("application/vnd.ms-excel"), "xls"),
    XLSX(MediaType.valueOf("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"), "xlsx"),
    PPT(MediaType.valueOf("application/vnd.ms-powerpoint"), "ppt"),
    PPTX(MediaType.valueOf("application/vnd.openxmlformats-officedocument.presentationml.presentation"), "pptx"),
    TXT(MediaType.TEXT_PLAIN, "txt"),
    CSV(MediaType.valueOf("text/csv"), "csv"),

    // Videos
    MP4(MediaType.valueOf("video/mp4"), "mp4"),
    MPEG(MediaType.valueOf("video/mpeg"), "mpeg", "mpg"),
    OGG_VIDEO(MediaType.valueOf("video/ogg"), "ogv"),
    WEBM(MediaType.valueOf("video/webm"), "webm"),
    AVI(MediaType.valueOf("video/x-msvideo"), "avi"),
    MOV(MediaType.valueOf("video/quicktime"), "mov"),
    WMV(MediaType.valueOf("video/x-ms-wmv"), "wmv"),

    // Audio
    MP3(MediaType.valueOf("audio/mpeg"), "mp3"),
    WAV(MediaType.valueOf("audio/wav"), "wav"),
    OGG_AUDIO(MediaType.valueOf("audio/ogg"), "oga", "ogg"),
    FLAC(MediaType.valueOf("audio/flac"), "flac"),
    AAC(MediaType.valueOf("audio/aac"), "aac"),
    WMA(MediaType.valueOf("audio/x-ms-wma"), "wma"),

    // Archives
    ZIP(MediaType.valueOf("application/zip"), "zip"),
    RAR(MediaType.valueOf("application/vnd.rar"), "rar"),
    TAR(MediaType.valueOf("application/x-tar"), "tar"),
    GZIP(MediaType.valueOf("application/gzip"), "gz");

    private final MediaType mediaType;
    private final List<String> extensions;

    // ✅ Maps estáticos para busca O(1)
    private static final Map<String, MediaTypeExtension> EXTENSION_MAP;
    private static final Map<MediaType, MediaTypeExtension> MEDIA_TYPE_MAP;

    static {
        // Inicialização eager dos maps
        EXTENSION_MAP = Arrays.stream(values())
                .flatMap(type -> type.extensions.stream()
                        .map(ext -> Map.entry(ext.toLowerCase(), type)))
                .collect(Collectors.toUnmodifiableMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (existing, replacement) -> existing // Em caso de duplicata, mantém o primeiro
                ));

        MEDIA_TYPE_MAP = Arrays.stream(values())
                .collect(Collectors.toUnmodifiableMap(
                        MediaTypeExtension::getMediaType,
                        e -> e,
                        (existing, replacement) -> existing
                ));
    }

    MediaTypeExtension(MediaType mediaType, String... extensions) {
        this.mediaType = mediaType;
        this.extensions = Arrays.asList(extensions);
    }

    // ✅ Busca O(1) por MediaType
    public static Optional<MediaTypeExtension> fromMediaType(MediaType mediaType) {
        if (mediaType == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(MEDIA_TYPE_MAP.get(mediaType));
    }

    // ✅ Busca O(1) por extensão
    public static Optional<MediaTypeExtension> fromExtension(String extension) {
        if (extension == null || extension.isBlank()) {
            return Optional.empty();
        }

        String normalized = extension.toLowerCase().replace(".", "").trim();
        return Optional.ofNullable(EXTENSION_MAP.get(normalized));
    }

    // ✅ Métodos de utilidade (também otimizados)
    public static boolean isExtensionSupported(String extension) {
        return fromExtension(extension).isPresent();
    }

    public static boolean isMediaTypeSupported(MediaType mediaType) {
        return fromMediaType(mediaType).isPresent();
    }

    public String getPrimaryExtension() {
        return extensions.get(0);
    }

    public String generateFilename(String baseName) {
        return baseName + "." + getPrimaryExtension();
    }

    public String getMediaTypeString() {
        return mediaType.toString();
    }

    // ✅ Método para debug (opcional)
    public static Map<String, MediaTypeExtension> getExtensionMap() {
        return EXTENSION_MAP;
    }

    public static Map<MediaType, MediaTypeExtension> getMediaTypeMap() {
        return MEDIA_TYPE_MAP;
    }
}
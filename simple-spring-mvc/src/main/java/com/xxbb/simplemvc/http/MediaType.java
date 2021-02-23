package com.xxbb.simplemvc.http;

import org.springframework.lang.Nullable;
import org.springframework.util.*;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.util.*;

public class MediaType extends MimeType implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final MediaType APPLICATION_FORM_URLENCODED;
    public static final String APPLICATION_FORM_URLENCODED_VALUE = "application/x-www-form-urlencoded";
    public static final MediaType APPLICATION_JSON;
    public static final String APPLICATION_JSON_VALUE = "application/json";
    public static final MediaType APPLICATION_OCTET_STREAM;
    public static final String APPLICATION_OCTET_STREAM_VALUE = "application/octet-stream";
    public static final MediaType MULTIPART_FORM_DATA;
    public static final String MULTIPART_FORM_DATA_VALUE = "multipart/form-data";

    public static final String PARAM_QUALITY_FACTOR = "q";

    static {
        APPLICATION_FORM_URLENCODED = new MediaType("application", "x-www-form-urlencoded");
        APPLICATION_JSON = new MediaType("application", "json");
        APPLICATION_OCTET_STREAM = new MediaType("application", "octet-stream");
        MULTIPART_FORM_DATA = new MediaType("multipart", "form-data");
    }

    public MediaType(String type) {
        super(type);
    }

    public MediaType(String type, String subtype) {
        super(type, subtype, Collections.emptyMap());
    }

    public MediaType(String type, String subtype, Charset charset) {
        super(type, subtype, charset);
    }

    public MediaType(String type, String subtype, double qualityValue) {
        super(type, subtype, Collections.singletonMap(PARAM_QUALITY_FACTOR, Double.toString(qualityValue)));
    }

    public MediaType(MediaType other, Charset charset) {
        super(other, charset);
    }

    public MediaType(MediaType other, @Nullable Map<String, String> parameters) {
        super(other.getType(), other.getSubtype(), parameters);
    }

    public MediaType(String type, String subtype, @Nullable Map<String, String> parameters) {
        super(type, subtype, parameters);
    }

    @Override
    protected void checkParameters(String attribute, String value) {
        super.checkParameters(attribute, value);
        if (PARAM_QUALITY_FACTOR.equals(attribute)) {
            value = unquote(value);
            double d = Double.parseDouble(value);
            Assert.isTrue(d >= 0D && d <= 1D,
                    "Invalid quality value \"" + value + "\" should between 0.0 and 1.0");
        }
    }

    public double getQualityValue() {
        String qualityFactor = getParameter(PARAM_QUALITY_FACTOR);
        return (qualityFactor != null ? Double.parseDouble(qualityFactor) : 1D);
    }

    public boolean includes(@Nullable MediaType other) {
        return super.includes(other);
    }

    public boolean isCompatibleWith(@Nullable MediaType other) {
        return super.isCompatibleWith(other);
    }

    public MediaType copyQualityValue(MediaType mediaType) {
        if (!mediaType.getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
            return this;
        }
        Map<String, String> params = new LinkedHashMap<>(getParameters());
        params.put(PARAM_QUALITY_FACTOR, mediaType.getParameters().get(PARAM_QUALITY_FACTOR));
        return new MediaType(this, params);
    }

    public MediaType removeQualityValue() {
        if (!getParameters().containsKey(PARAM_QUALITY_FACTOR)) {
            return this;
        }
        Map<String, String> params = new LinkedHashMap<>(getParameters());
        params.remove(PARAM_QUALITY_FACTOR);
        return new MediaType(this, params);
    }

    public static MediaType valueOf(String value) {
        return parseMediaType(value);
    }

    public static MediaType parseMediaType(String mediaType) {
        MimeType type;
        try {
            type = MimeTypeUtils.parseMimeType(mediaType);
        } catch (InvalidMimeTypeException ex) {
            throw new InvalidMediaTypeException(ex);
        }

        try {
            return new MediaType(type.getType(), type.getSubtype(), type.getParameters());
        } catch (IllegalArgumentException ex) {
            throw new InvalidMediaTypeException(mediaType, ex.getMessage());
        }
    }

    public static List<MediaType> parseMediaTypes(@Nullable String mediaTypes) {
        if (!StringUtils.hasLength(mediaTypes)) {
            return Collections.emptyList();
        }

        List<String> tokenizedTypes = MimeTypeUtils.tokenize(mediaTypes);
        List<MediaType> result = new ArrayList<>(tokenizedTypes.size());
        for (String type : tokenizedTypes) {
            if (StringUtils.hasText(type)) {
                result.add(parseMediaType(type));
            }
        }
        return result;
    }

    public static List<MediaType> parseMediaTypes(@Nullable List<String> mediaTypes) {
        if (CollectionUtils.isEmpty(mediaTypes)) {
            return Collections.emptyList();
        } else if (mediaTypes.size() == 1) {
            return parseMediaTypes(mediaTypes.get(0));
        } else {
            List<MediaType> result = new ArrayList<>(8);
            for (String mediaType : mediaTypes) {
                result.addAll(parseMediaTypes(mediaType));
            }
            return result;
        }
    }

    public static List<MediaType> asMediaTypes(List<MimeType> mimeTypes) {
        List<MediaType> mediaTypes = new ArrayList<>(mimeTypes.size());
        for (MimeType mimeType : mimeTypes) {
            mediaTypes.add(MediaType.asMediaType(mimeType));
        }
        return mediaTypes;
    }

    public static MediaType asMediaType(MimeType mimeType) {
        if (mimeType instanceof MediaType) {
            return (MediaType) mimeType;
        }
        return new MediaType(mimeType.getType(), mimeType.getSubtype(), mimeType.getParameters());
    }

    public static String toString(Collection<MediaType> mediaTypes) {
        return MimeTypeUtils.toString(mediaTypes);
    }

    public static void sortBySpecificity(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            mediaTypes.sort(SPECIFICITY_COMPARATOR);
        }
    }

    public static void sortByQualityValue(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            mediaTypes.sort(SPECIFICITY_COMPARATOR);
        }
    }

    public static void sortBySpecificityAndQuality(List<MediaType> mediaTypes) {
        Assert.notNull(mediaTypes, "'mediaTypes' must not be null");
        if (mediaTypes.size() > 1) {
            mediaTypes.sort(MediaType.SPECIFICITY_COMPARATOR.thenComparing(MediaType.QUALITY_VALUE_COMARATOR));
        }
    }

    public static final Comparator<MediaType> QUALITY_VALUE_COMARATOR = (m1, m2) -> {
        double q1 = m1.getQualityValue();
        double q2 = m2.getQualityValue();

        int qualityComparison = Double.compare(q2, q1);
        if (qualityComparison != 0) {
            return qualityComparison;
        }
        if (m1.isWildcardType() && !m2.isWildcardType()) {
            return 1;
        }
        if (m2.isWildcardType() && !m1.isWildcardType()) {
            return -1;
        }
        if (m1.getType().equals(m2.getType())) {
            return 0;
        } else {
            if (m1.isWildcardSubtype() && !m2.isWildcardSubtype()) {
                return 1;
            } else if (m2.isWildcardSubtype() && !m1.isWildcardSubtype()) {
                return -1;
            } else if (!m1.isWildcardSubtype() && !m2.isWildcardSubtype()) {
                return 0;
            } else {
                int paramsSize1 = m1.getParameters().size();
                int paramsSize2 = m2.getParameters().size();
                return Integer.compare(paramsSize2, paramsSize1);
            }
        }

    };

    public static final Comparator<MediaType> SPECIFICITY_COMPARATOR = new SpecificityComparator<MediaType>() {
        @Override
        protected int compareParameters(MediaType mediaType1, MediaType mediaType2) {
            double q1 = mediaType1.getQualityValue();
            double q2 = mediaType2.getQualityValue();
            int qualityComparison = Double.compare(q1, q2);
            if (qualityComparison != 0) {
                return qualityComparison;
            }
            return super.compareParameters(mediaType1, mediaType2);
        }
    };
}


package com.collectto.api_collectto.domain.shared;

// This class is likes Objects.requireNonNull(), but with custom methods to validate domain entities and throw IllegalArgumentException with custom messages.
public class DomainValidator {

    public static <T> T requireNonNull(T obj, String message) {
            if (obj == null) {
                throw new IllegalArgumentException(message);
            }
            return obj;
        }

        public static String requireNonBlank(String value, String message) {
            if (value == null || value.isBlank()) {
                throw new IllegalArgumentException(message);
            }
            return value;
        }

        public static int requireNonNegative(int value, String message) {
            if (value < 0) {
                throw new IllegalArgumentException(message);
            }
            return value;
        }
}

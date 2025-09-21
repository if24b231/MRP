package at.fhtw.mrp.utils;

import at.fhtw.restserver.http.Method;

public record Mapping(Method methodType, String path, boolean authenticationNeeded) {
}

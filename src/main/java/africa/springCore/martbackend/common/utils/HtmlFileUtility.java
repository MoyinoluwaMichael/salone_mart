package africa.springCore.martbackend.common.utils;


import africa.springCore.martbackend.infrastructure.exception.MartException;

import java.io.*;
import java.util.stream.Collectors;

import static africa.springCore.martbackend.common.Message.FAILED_TO_GET_ACTIVATION_LINK;


public class HtmlFileUtility {
    public static String getFileTemplate(String filePath) throws MartException {
        try(BufferedReader reader =
                    new BufferedReader(new FileReader(filePath))){
            return  reader.lines().collect(Collectors.joining());
        }catch (IOException exception){
            throw new MartException(FAILED_TO_GET_ACTIVATION_LINK);
        }
    }

    public static String getFileTemplateFromClasspath(String resourcePath) throws MartException {
        try (InputStream inputStream = HtmlFileUtility.class.getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new MartException("Resource not found: " + resourcePath);
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.lines().collect(Collectors.joining());
            }
        } catch (IOException | MartException exception) {
            throw new MartException("Failed to read resource: " + resourcePath + " - " + exception.getMessage());
        }
    }
}

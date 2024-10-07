import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

public class AnnotationExample {

    // Define a simple marker annotation
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface SimpleAnnotation {
    }

    // Define an annotation that includes data
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface DataAnnotation {
        String description() default "No description";

        int value() default 0;
    }

    // Use the SimpleAnnotation
    @SimpleAnnotation
    public void simpleMethod() {
        System.out.println("Simple method is running.");
    }

    // Use the DataAnnotation with data
    @DataAnnotation(description = "Detailed method desc.", value = 42)
    public void detailedMethod() {
        System.out.println("Detailed method is running.");
    }

    public static void main(String[] args) {
        try {
            Method[] methods = AnnotationExample.class.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(SimpleAnnotation.class)) {
                    System.out.println(method.getName() + " has SimpleAnnotation.");
                }

                if (method.isAnnotationPresent(DataAnnotation.class)) {
                    DataAnnotation annotation = method.getAnnotation(DataAnnotation.class);
                    System.out.println(method.getName() + " has DataAnnotation with description: '"
                            + annotation.description() + "' and value: '" + annotation.value() + "'");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

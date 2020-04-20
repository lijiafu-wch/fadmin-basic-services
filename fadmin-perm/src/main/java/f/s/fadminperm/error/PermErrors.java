package f.s.fadminperm.error;

import f.s.jerror.BaseError;
import f.s.jerror.annotation.Errors;
import org.springframework.stereotype.Component;

@Component
@Errors
public interface PermErrors {
    BaseError MissingArgument(String argName);
    BaseError OldPasswordError(String argName);
}
package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class IsEmptyException extends BusinessException {
    public IsEmptyException(String field, String object) {
        super("""
                El campo '%s' en el '%s' esta vacio.
                """.formatted(field, object)
        );
    }
}

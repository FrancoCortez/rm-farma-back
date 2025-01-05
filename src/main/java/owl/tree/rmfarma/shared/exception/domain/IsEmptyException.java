package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class IsEmptyException extends BusinessException {
    public IsEmptyException(String field, String object) {
        super("""
                The field '%s' in the object '%s' is empty.
                """.formatted(field, object)
        );
    }
}

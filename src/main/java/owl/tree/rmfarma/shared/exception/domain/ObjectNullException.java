package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class ObjectNullException extends BusinessException {
    public ObjectNullException(String object) {
        super("""
                The object '%s' is null.
                """.formatted(object)
        );
    }
}

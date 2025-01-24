package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class ExistsException extends BusinessException {
    public ExistsException(String field, String object, String key) {
        super("""
                The %s already exists in the system with the %s with key %s.
                """.formatted(object, field, key)
        );
    }
}

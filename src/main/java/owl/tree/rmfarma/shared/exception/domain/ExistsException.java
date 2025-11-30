package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class ExistsException extends BusinessException {
    public ExistsException(String field, String object, String key) {
        super("""
                El %s ya existe en el sistema con el %s con clave %s.
                """.formatted(object, field, key)
        );
    }
}

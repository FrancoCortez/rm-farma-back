package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class NotFoundException extends BusinessException {
    public NotFoundException(String object, String entity) {
        super("""
                El objeto %s con id %s no fue encontrado.
                """.formatted(object, entity)
        );
    }
}

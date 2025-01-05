package owl.tree.rmfarma.shared.exception.domain;

import owl.tree.rmfarma.shared.exception.base.BusinessException;

public class NotFoundException extends BusinessException {
    public NotFoundException(String object, String entity) {
        super("""
                The %s object for id %s was not found.
                """.formatted(object, entity)
        );
    }
}

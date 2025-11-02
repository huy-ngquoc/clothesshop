package vn.uit.clothesshop.infrastructure.storage;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import vn.uit.clothesshop.shared.port.storage.event.ImageAdded;
import vn.uit.clothesshop.shared.port.storage.event.ImageDeleted;
import vn.uit.clothesshop.shared.port.storage.event.ImageReplaced;

// package: com.yourapp.infrastructure.storage
@Component
public class ImageStorageTxHandler {

    private final LocalImageStorage storage;

    public ImageStorageTxHandler(final LocalImageStorage storage) {
        this.storage = storage;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onAddedRollback(final ImageAdded e) {
        if (e.fileName() != null) {
            storage.handleDeleteUploadFile(e.fileName(), e.folder().sub());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onReplacedCommit(final ImageReplaced e) {
        if (e.oldFileName() != null && !e.oldFileName().isBlank()) {
            storage.handleDeleteUploadFile(e.oldFileName(), e.folder().sub());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    public void onReplacedRollback(final ImageReplaced e) {
        if (e.newFileName() != null) {
            storage.handleDeleteUploadFile(e.newFileName(), e.folder().sub());
        }
    }

    // DELETE entity: chỉ xóa file sau khi commit DB
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onDeletedCommit(final ImageDeleted e) {
        if (e.fileName() != null && !e.fileName().isBlank()) {
            storage.handleDeleteUploadFile(e.fileName(), e.folder().sub());
        }
    }
}

import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPrixBoisson } from '../prix-boisson.model';
import { PrixBoissonService } from '../service/prix-boisson.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './prix-boisson-delete-dialog.component.html',
})
export class PrixBoissonDeleteDialogComponent {
  prixBoisson?: IPrixBoisson;

  constructor(protected prixBoissonService: PrixBoissonService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.prixBoissonService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

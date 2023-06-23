import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICompteCaisse } from '../compte-caisse.model';
import { CompteCaisseService } from '../service/compte-caisse.service';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';

@Component({
  templateUrl: './compte-caisse-delete-dialog.component.html',
})
export class CompteCaisseDeleteDialogComponent {
  compteCaisse?: ICompteCaisse;

  constructor(protected compteCaisseService: CompteCaisseService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compteCaisseService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}

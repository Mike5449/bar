import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrix } from '../prix.model';

@Component({
  selector: 'jhi-prix-detail',
  templateUrl: './prix-detail.component.html',
})
export class PrixDetailComponent implements OnInit {
  prix: IPrix | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prix }) => {
      this.prix = prix;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

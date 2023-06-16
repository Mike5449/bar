import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPrixBoisson } from '../prix-boisson.model';

@Component({
  selector: 'jhi-prix-boisson-detail',
  templateUrl: './prix-boisson-detail.component.html',
})
export class PrixBoissonDetailComponent implements OnInit {
  prixBoisson: IPrixBoisson | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ prixBoisson }) => {
      this.prixBoisson = prixBoisson;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

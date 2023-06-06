import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ISalary } from '../salary.model';

@Component({
  selector: 'jhi-salary-detail',
  templateUrl: './salary-detail.component.html',
})
export class SalaryDetailComponent implements OnInit {
  salary: ISalary | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ salary }) => {
      this.salary = salary;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

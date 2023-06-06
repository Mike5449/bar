import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ProductPriceDetailComponent } from './product-price-detail.component';

describe('ProductPrice Management Detail Component', () => {
  let comp: ProductPriceDetailComponent;
  let fixture: ComponentFixture<ProductPriceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ProductPriceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ productPrice: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ProductPriceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ProductPriceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load productPrice on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.productPrice).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});

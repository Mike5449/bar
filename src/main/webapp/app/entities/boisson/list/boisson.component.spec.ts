import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { BoissonService } from '../service/boisson.service';

import { BoissonComponent } from './boisson.component';

describe('Boisson Management Component', () => {
  let comp: BoissonComponent;
  let fixture: ComponentFixture<BoissonComponent>;
  let service: BoissonService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [RouterTestingModule.withRoutes([{ path: 'boisson', component: BoissonComponent }]), HttpClientTestingModule],
      declarations: [BoissonComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(BoissonComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(BoissonComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(BoissonService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.boissons?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to boissonService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getBoissonIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getBoissonIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});

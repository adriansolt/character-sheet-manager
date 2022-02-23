jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ArmorPieceService } from '../service/armor-piece.service';

import { ArmorPieceDeleteDialogComponent } from './armor-piece-delete-dialog.component';

describe('ArmorPiece Management Delete Component', () => {
  let comp: ArmorPieceDeleteDialogComponent;
  let fixture: ComponentFixture<ArmorPieceDeleteDialogComponent>;
  let service: ArmorPieceService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ArmorPieceDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(ArmorPieceDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ArmorPieceDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ArmorPieceService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});

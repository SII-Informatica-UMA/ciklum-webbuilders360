import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {ContactosService } from './contactos.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';

describe('El componente principal', () => {
  const mockService = {
    getContactos: () => {
      return [
        {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Hombre'},
        {id: 2, nombre: 'Ana', apellidos: 'García', email: '', telefono: '', genero: 'Mujer'}]
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      providers: [
        {provide: ContactosService, useValue: mockService},
        NgbModal]
    }).compileComponents();
  });

  it('debe mostrar a Juan en primer lugar en la lista de botones', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    compiled.querySelectorAll('button.list-group-item').forEach((element, index) => {
      if (index == 0) {
        expect(element.textContent).toContain("Juan");
      }
    });
  });

  it('debe mostrar a Ana em segundo lugar en la lista de botones', () => {
    const fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    compiled.querySelectorAll('button.list-group-item').forEach((element, index) => {
      if (index == 1) {
        expect(element.textContent).toContain("Ana");
      }
    });
  });
});

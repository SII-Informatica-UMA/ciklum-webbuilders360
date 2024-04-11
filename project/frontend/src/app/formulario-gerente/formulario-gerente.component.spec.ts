import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormularioGerenteComponent } from './formulario-gerente.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { UsuariosService } from '../services/usuarios.service';
import { Usuario } from '../entities/usuario';
import { of } from 'rxjs';
import { CommonModule } from '@angular/common';

describe('FormularioGerenteComponent', () => {
  let component: FormularioGerenteComponent;
  let fixture: ComponentFixture<FormularioGerenteComponent>;
  let usuariosServiceSpy: jasmine.SpyObj<UsuariosService>;

  beforeEach(async () => {
    const spy = jasmine.createSpyObj('UsuariosService', ['getUsuarios']);

    await TestBed.configureTestingModule({
      declarations: [ FormularioGerenteComponent ],
      providers: [
        NgbActiveModal,
        { provide: UsuariosService, useValue: spy }
      ],
      imports: [CommonModule, FormsModule]
    })
    .compileComponents();

    usuariosServiceSpy = TestBed.inject(UsuariosService) as jasmine.SpyObj<UsuariosService>;
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FormularioGerenteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call actualizarUsuarios on ngOnInit', () => {
    const spy = spyOn(component, 'actualizarUsuarios');
    component.ngOnInit();
    expect(spy).toHaveBeenCalled();
  });

  it('should set usuarios correctly on actualizarUsuarios', () => {
    const mockUsuarios: Usuario[] = [
      {id: 1, nombre: "nombre1", apellido1: "apellido11", apellido2: "apellido21", email: "1@uma.es", password: "1", administrador: false},
      {id: 2, nombre: "nombre2", apellido1: "apellido12", apellido2: "apellido22", email: "2@uma.es", password: "2", administrador: false}
    ];
    usuariosServiceSpy.getUsuarios.and.returnValue(of(mockUsuarios));

    component.actualizarUsuarios();
    expect(component.usuarios).toEqual(mockUsuarios);
  });

  it('should set usuarioSeleccionado correctly on elegirUsuario', () => {
    const mockUsuario: Usuario = {id: 3, nombre: "nombre3", apellido1: "apellido13", apellido2: "apellido23", email: "3@uma.es", password: "3", administrador: false};

    component.elegirUsuario(mockUsuario);
    expect(component.usuarioSeleccionado).toEqual(mockUsuario);
  });

  it('should close modal with gerente on guardarGerente', () => {
    const mockGerente = { idUsuario: 1, empresa: 'Test Company', id: 1 };
    const modalCloseSpy = spyOn(component.modal, 'close');

    component.gerente = mockGerente;
    component.usuarioSeleccionado = {id: 4, nombre: "nombre4", apellido1: "apellido14", apellido2: "apellido24", email: "4@uma.es", password: "4", administrador: false};
    component.guardarGerente();
    expect(modalCloseSpy).toHaveBeenCalledWith(mockGerente);
  });
});

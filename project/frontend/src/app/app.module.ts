import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DetalleCentroComponent } from './detalle-centro/detalle-centro.component';
import { FormularioCentroComponent } from './formulario-centro/formulario-centro.component';

import { DetalleGerenteComponent } from './detalle-gerente/detalle-gerente.component';
import { FormularioGerenteComponent } from './formulario-gerente/formulario-gerente.component';

@NgModule({
  declarations: [
    AppComponent,
    DetalleCentroComponent,
    DetalleGerenteComponent,
    FormularioCentroComponent,
    FormularioGerenteComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }

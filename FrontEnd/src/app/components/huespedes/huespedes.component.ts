import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import Swal from 'sweetalert2';
import { HuespedRequest, HuespedResponse } from '../../models/Huesped.model';
import { HuespedesService } from '../../services/huesped.service';
import { AuthService } from '../../services/auth.service';
declare var bootstrap: any;

@Component({
  selector: 'app-huespedes',
  standalone: false,
  templateUrl: './huespedes.component.html',
  styleUrl: './huespedes.component.css'
})
export class HuespedesComponent implements OnInit, AfterViewInit{

  listaHuespedes: HuespedResponse[] = [];

  isEditMode: boolean = false;
  selectedHuesped: HuespedResponse | null = null;
  showActions: boolean = false;
  modalText: string = 'Registrar huésped';

  @ViewChild('huespedModalRef')
  huespedModalEl!: ElementRef;
  huespedForm: FormGroup;

  private modalInstance!: any;


  constructor(private fb: FormBuilder, private huespedService: HuespedesService,
    private authService: AuthService
  ){
    this.huespedForm = this.fb.group({
      id: [null],
      nombre: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(2), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/)]],
      apellidoPaterno: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(2), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/)]],
      apellidoMaterno: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(2), Validators.pattern(/^[a-zA-ZáéíóúÁÉÍÓÚñÑ\s]+$/)]],
      edad: [null, [Validators.required, Validators.min(18), Validators.max(100)]],
      email: ['', [Validators.required, Validators.minLength(5), Validators.email, Validators.pattern(/^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/)]],
      telefono: ['', [Validators.required, Validators.maxLength(10), Validators.minLength(10), Validators.pattern(/^[0-9]+$/)]],
      documento: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(1), Validators.pattern(/^(?!\s*$).+/)]],
      nacionalidad: ['', [Validators.required, Validators.maxLength(50), Validators.minLength(1), Validators.pattern(/^(?!\s*$).+/)]]
    })
  }

  ngOnInit(): void {
    this.listarHuespedes();
    /*
    if(this.authService.hasRole(Roles.ADMIN)){
      this.showActions = true;
    } */
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.huespedModalEl.nativeElement, {keyboard: false});
    this.huespedModalEl.nativeElement.addEventListener('hidden.bs.modal', () => {
      this.resetForm();
    })
  }

  listarHuespedes(): void{
    this.huespedService.getHuespedes().subscribe({
      next: resp => {
        this.listaHuespedes = resp;
      }
    })
  }

  resetForm(): void{
    this.isEditMode = false;
    this.selectedHuesped = null;
    this.huespedForm.reset();
  }

  toggleForm(): void{
    this.resetForm();
    this.modalText = 'Registrar huésped';
    this.modalInstance.show();
  }

  editHuesped(huesped: HuespedResponse): void{
    this.isEditMode = true;
    this.selectedHuesped = huesped;
    this.modalText = 'Editando huésped: ' + huesped.nombre;

    this.huespedForm.patchValue({...huesped});
    this.modalInstance.show();
  }

   onSubmit(): void{
    if(this.huespedForm.invalid) return;

    const huespedData: HuespedRequest = this.huespedForm.value;

    if(this.isEditMode && this.selectedHuesped){
      this.huespedService.putHuesped(huespedData, this.selectedHuesped.id).subscribe({
        next: registro => {
          const index: number = this.listaHuespedes.findIndex(p => p.id === this.selectedHuesped!.id);
          if(index !== -1) this.listaHuespedes[index] = registro;
          Swal.fire('Actualizado', 'Huésped actualizado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    } else {
      this.huespedService.postHuesped(huespedData).subscribe({
        next: registro => {
          this.listaHuespedes.push(registro);
          Swal.fire('Registrado', 'Huésped registrado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    }
  }

  deleteHuesped(idHuesped: number): void{
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El huésped será eliminado permanente',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar' 
    }).then(result => {
      if(result.isConfirmed){
        this.huespedService.deleteHuesped(idHuesped).subscribe({
          next: () => {
            this.listaHuespedes = this.listaHuespedes.filter(p => p.id !== idHuesped);
            Swal.fire('Eliminado', 'Huésped eliminado correctamente', 'success');
          },
            error: (error) => {
              console.error('Error al eliminar huésped:', error);          
          }
        });
      }
    });
  }

}

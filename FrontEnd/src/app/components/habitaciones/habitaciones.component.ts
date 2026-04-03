import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HabitacionRequest, HabitacionResponse } from '../../models/Habitacion.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HabitacionesSerivice } from '../../services/habitacion.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { Rol } from '../../constants/Rol';
import { formatNumber } from '@angular/common';

declare var bootstrap: any;

@Component({
  selector: 'app-habitaciones',
  standalone: false,
  templateUrl: './habitaciones.component.html',
  styleUrl: './habitaciones.component.css'
})
export class HabitacionesComponent implements OnInit, AfterViewInit {

  listaHabitaciones: HabitacionResponse[] = [];

  isEditMode: boolean = false;
  selectedHabitacion: HabitacionResponse | null = null;
  showActions: boolean = false;
  modalText: string = 'Registrar Habitación';

  @ViewChild('habitacionModalRef')
  habitacionModalEl!: ElementRef;
  habitacionForm: FormGroup;

  private modalInstance!: any;

  constructor(private fb: FormBuilder, private habitacionService: HabitacionesSerivice,
    private authService: AuthService
  ) {
    this.habitacionForm = this.fb.group({
      id: [null],
      numero: [null, [Validators.required, Validators.min(1)]],
      tipo: ['', [Validators.required]],
      precio: [null, [Validators.required, Validators.min(1)]],
      capacidad: [null, [Validators.required, Validators.min(1), Validators.max(10)]],
      idEstadoHabitacion: [1]
    })
  }

  ngOnInit(): void {
    this.listarHabitaciones();
    if (this.authService.hasRole(Rol.ADMIN)) {
      this.showActions = true;
    }
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.habitacionModalEl.nativeElement, { keyboard: false });
    this.habitacionModalEl.nativeElement.addEventListener('hidden.bs.modal', () => {
      this.resetForm();
    })
  }

  listarHabitaciones(): void {
    this.habitacionService.getHabitaciones().subscribe({
      next: resp => {
        this.listaHabitaciones = resp;
      }
    })
  }

  resetForm(): void {
    this.isEditMode = false;
    this.selectedHabitacion = null;
    this.habitacionForm.reset();
  }

  toggleForm(): void {
    this.resetForm();
    this.modalText = 'Registrar habitación';
    this.modalInstance.show();
  }

  editHabitacion(habitacion: HabitacionResponse): void {
    this.isEditMode = true;
    this.selectedHabitacion = habitacion;
    this.modalText = 'Editando habitación: ' + habitacion.numero;

    this.habitacionForm.patchValue({
      ...habitacion,
      tipo: habitacion.tipo,
      idEstadoHabitacion: this.mapearEstado(habitacion.estadoHabitacion)
    });
    this.modalInstance.show();
  }

  onSubmit(): void {
    if (this.habitacionForm.invalid) return;

    const formValue = this.habitacionForm.value;
    
    const habitacionData: HabitacionRequest = {
      ...formValue
    }
    console.log(habitacionData);

    if (this.isEditMode && this.selectedHabitacion) {
      this.habitacionService.putHabitacion(habitacionData, this.selectedHabitacion.id).subscribe({
        next: registro => {
          const index: number = this.listaHabitaciones.findIndex(p => p.id === this.selectedHabitacion!.id);
          if (index !== -1) this.listaHabitaciones[index] = registro;
          Swal.fire('Actualizado', 'Habitación actualizado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    } else {
      this.habitacionService.postHabitacion(habitacionData).subscribe({
        next: registro => {
          this.listaHabitaciones.push(registro);
          Swal.fire('Registrado', 'Habitación registrado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    }
  }

  deleteHabitacion(idHabitacion: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El habitación será eliminado permanente',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.habitacionService.deleteHabitacion(idHabitacion).subscribe({
          next: () => {
            this.listaHabitaciones = this.listaHabitaciones.filter(p => p.id !== idHabitacion);
            Swal.fire('Eliminado', 'Habitación eliminado correctamente', 'success');
          },
          error: (error) => {
            console.error('Error al eliminar habitación:', error);
          }
        });
      }
    });
  }
  
   private mapearEstado(estado: string): string {
    if (estado === 'Lista para asignarse') return '1';
    if(estado === 'Asignada a una reserva') return '2';
    if (estado === 'En limpieza') return '3';
    if (estado === 'En reparación') return '4';
    return '1';
  }
}

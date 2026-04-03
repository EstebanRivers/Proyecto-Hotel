import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ReservaRequest, ReservaResponse } from '../../models/Reserva.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReservasService } from '../../services/reserva.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { Rol } from '../../constants/Rol';

declare var bootstrap: any;

@Component({
  selector: 'app-reservas',
  standalone: false,
  templateUrl: './reservas.component.html',
  styleUrl: './reservas.component.css'
})

export class ReservasComponent implements OnInit, AfterViewInit {

  listaReservas: ReservaResponse[] = [];

  opcionIn: boolean = false;
  opcionOut: boolean = false;
  opcionCancel: boolean = false;
  botonEditar: boolean = false;

  isEditMode: boolean = false;
  selectedReserva: ReservaResponse | null = null;
  showActions: boolean = false;
  modalText: string = 'Registrar reserva';

  @ViewChild('reservaModalRef')
  reservaModalEl!: ElementRef;
  reservaForm: FormGroup;

  private modalInstance!: any;

  constructor(private fb: FormBuilder, private reservaService: ReservasService,
    private authService: AuthService
  ) {
    this.reservaForm = this.fb.group({
      id: [null],
      idHuesped: [null, [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)]],
      idHabitacion: [null, [Validators.required, Validators.min(1), Validators.pattern(/^[0-9]+$/)]],
      fechaEntrada: ['', [Validators.required]],
      fechaSalida: ['', [Validators.required]],
      idEstadoReserva: [1]
    })
  }

  ngOnInit(): void {
    this.listarReservas();
    if(this.authService.hasRole(Rol.ADMIN)){
      this.showActions = true;
    }
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.reservaModalEl.nativeElement, { keyboard: false });
    this.reservaModalEl.nativeElement.addEventListener('hidden.bs.modal', () => {
      this.resetForm();
    })
  }

  listarReservas(): void {
    this.reservaService.getReservas().subscribe({
      next: resp => {
        this.listaReservas = resp;
      }
    })
  }

  resetForm(): void {
    this.isEditMode = false;
    this.selectedReserva = null;
    this.reservaForm.reset();
  }

  toggleForm(): void {
    this.resetForm();
    this.modalText = 'Registrar reserva';
    this.modalInstance.show();
  }

  editReserva(reserva: ReservaResponse): void {
    this.isEditMode = true;
    this.selectedReserva = reserva;
    this.modalText = 'Editando reserva: ' + reserva.id;

    this.reservaForm.get('idHuesped')?.clearValidators();
    this.reservaForm.get('idHabitacion')?.clearValidators();

    switch (reserva.estadoReserva) {
      case 'Reserva creada':
        this.opcionIn = true;
        this.opcionOut = false;
        this.opcionCancel = true;
        break;
      case 'Check-in realizado':
        this.opcionIn = false;
        this.opcionOut = true;
        this.opcionCancel = false;
        break;
      case 'Check-out realizado':
        this.opcionIn = false;
        this.opcionOut = false;
        this.opcionCancel = false;
        break;
      default:
        this.opcionIn = false;
        this.opcionOut = false;
        this.opcionCancel = false;
        break;
    }

    setTimeout(() => {
      this.reservaForm.patchValue({
        ...reserva,
        idHabitacion: reserva.habitacion.id,
        idHuesped: reserva.huesped.id,
        fechaEntrada: this.formatoSalida(reserva.fechaEntrada),
        fechaSalida: this.formatoSalida(reserva.fechaSalida),
        idEstadoReserva: this.mapearEstado(reserva.estadoReserva)
      });
      this.modalInstance.show();
    }, 0);
  }

  onSubmit(): void {
    if (this.reservaForm.invalid) return;
    const formValue = this.reservaForm.value;

    const reservaData: ReservaRequest = {
      ...formValue,
      fechaEntrada: this.formatoEntrada(formValue.fechaEntrada),
      fechaSalida: this.formatoEntrada(formValue.fechaSalida)
    };
    if (this.isEditMode && this.selectedReserva) {
      this.reservaService.putReserva(reservaData, this.selectedReserva.id).subscribe({
        next: registro => {
          const index: number = this.listaReservas.findIndex(p => p.id === this.selectedReserva!.id);
          if (index !== -1) this.listaReservas[index] = registro;
          Swal.fire('Actualizado', 'Reserva actualizado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    } else {
      this.reservaService.postReserva(reservaData).subscribe({
        next: registro => {
          this.listaReservas.push(registro);
          Swal.fire('Registrado', 'Reserva registrado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    }
  }

  deleteReserva(idReserva: number): void {
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'La reserva será eliminado permanente',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar'
    }).then(result => {
      if (result.isConfirmed) {
        this.reservaService.deleteReserva(idReserva).subscribe({
          next: () => {
            this.listaReservas = this.listaReservas.filter(p => p.id !== idReserva);
            Swal.fire('Eliminado', 'Reserva eliminado correctamente', 'success');
          },
          error: (error) => {
            console.error('Error al eliminar reserva:', error);
          }
        });
      }
    });
  }

  private mapearEstado(estado: string): string {
    if (estado === 'Reserva creada') return '2';
    if (estado === 'Check-in realizado') return '3';
    if (estado === 'Check-out realizado') return '4';
    if (estado === 'Reserva cancelada') return '4';
    return '1';
  }

  private formatoSalida(dateStr: string): string {
    if (!dateStr) return '';
    const [fecha, hora] = dateStr.split(' ');
    const [dia, mes, anio] = fecha.split('/');
    return `${anio}-${mes}-${dia}T${hora}`;
  }

  private formatoEntrada(fechaInput: string): string {
    if (!fechaInput) return '';
    const fecha = new Date(fechaInput);
    const dia = String(fecha.getDate()).padStart(2, '0');
    const mes = String(fecha.getMonth() + 1).padStart(2, '0');
    const anio = fecha.getFullYear();
    const horas = String(fecha.getHours()).padStart(2, '0');
    const minutos = String(fecha.getMinutes()).padStart(2, '0');
    return `${dia}/${mes}/${anio} ${horas}:${minutos}`;
  }

}
import { AfterViewInit, Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { UsuarioRequest, UsuarioResponse } from '../../models/Usuario.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { UsuariosService } from '../../services/usuario.service';
import { AuthService } from '../../services/auth.service';
import Swal from 'sweetalert2';
import { DescripcionRol, Rol } from '../../constants/Rol';

declare var bootstrap: any;

@Component({
  selector: 'app-usuarios',
  standalone: false,
  templateUrl: './usuarios.component.html',
  styleUrl: './usuarios.component.css'
})
export class UsuariosComponent implements OnInit, AfterViewInit {

  listaUsuarios: UsuarioResponse[] = [];

  isEditMode: boolean = false;
  selectedUsuario: UsuarioResponse | null = null;
  showActions: boolean = false;
  modalText: string = 'Registrar huésped';

  rol: string[] = Object.values(Rol);

  @ViewChild('usuarioModalRef')
  usuarioModalEl!: ElementRef;
  usuarioForm: FormGroup;

  private modalInstance!: any;

   constructor(private fb: FormBuilder, private usuarioService: UsuariosService,
    private authService: AuthService
  ){
    this.usuarioForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(4), Validators.maxLength(20)]],
      password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
      rol: ['', [Validators.required]]
    })
  }

  ngOnInit(): void {
    this.listarUsuarios();
    if(this.authService.hasRole(Rol.ADMIN)){
      this.showActions = true;
    } 
  }

  ngAfterViewInit(): void {
    this.modalInstance = new bootstrap.Modal(this.usuarioModalEl.nativeElement, {keyboard: false});
    this.usuarioModalEl.nativeElement.addEventListener('hidden.bs.modal', () => {
      this.resetForm();
    })
  }

  listarUsuarios(): void{
    this.usuarioService.getUsuarios().subscribe({
      next: resp => {
        this.listaUsuarios = resp;
      }
    })
  }

  resetForm(): void{
    this.isEditMode = false;
    this.selectedUsuario = null;
    this.usuarioForm.reset();
  }

  toggleForm(): void{
    this.resetForm();
    this.modalText = 'Registrar usuario';
    this.modalInstance.show();
  }

   transformarRol(rol: string): string{
    return DescripcionRol[rol as Rol] || 'Desconocido';
  }

  editUsuario(usuario: UsuarioResponse): void{
    this.isEditMode = true;
    this.selectedUsuario = usuario;
    this.modalText = 'Editando usuario: ' + usuario.username;

    this.usuarioForm.patchValue({...usuario});
    this.modalInstance.show();
  }

   onSubmit(): void{
    if(this.usuarioForm.invalid) return;

    const usuarioData: UsuarioRequest = this.usuarioForm.value;

    console.log(usuarioData);
    if(this.isEditMode && this.selectedUsuario){
      this.usuarioService.putUsuario(usuarioData, this.selectedUsuario.id).subscribe({
        next: registro => {
          const index: number = this.listaUsuarios.findIndex(p => p.id === this.selectedUsuario!.id);
          if(index !== -1) this.listaUsuarios[index] = registro;
          Swal.fire('Actualizado', 'Usuario actualizado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    } else {
      this.usuarioService.postUsuario(usuarioData).subscribe({
        next: registro => {
          this.listaUsuarios.push(registro);
          Swal.fire('Registrado', 'Usuario registrado correctamente', 'success');
          this.modalInstance.hide();
        }
      });
    }
  }

  deleteUsusario(idUsuario: number): void{
    Swal.fire({
      title: '¿Estás seguro?',
      text: 'El Usuario será eliminado permanente',
      showCancelButton: true,
      confirmButtonText: 'Sí, eliminar',
      cancelButtonText: 'Cancelar' 
    }).then(result => {
      if(result.isConfirmed){
        this.usuarioService.deleteUsuario(idUsuario).subscribe({
          next: () => {
            this.listaUsuarios = this.listaUsuarios.filter(p => p.id !== idUsuario);
            Swal.fire('Eliminado', 'Usuario eliminado correctamente', 'success');
          },
            error: (error) => {
              console.error('Error al eliminar usuario:', error);          
          }
        });
      }
    });
  }

}

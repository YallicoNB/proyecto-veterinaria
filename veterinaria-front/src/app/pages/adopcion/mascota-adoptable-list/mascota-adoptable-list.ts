import { Component, OnInit, inject } from '@angular/core';
import { RouterLink } from '@angular/router';
import { AdopcionService } from '../services/adopcion';
import { MascotaAdoptable } from '../../../models/mascota-adoptable.model';

@Component({
  selector: 'app-mascota-adoptable-list',
  imports: [RouterLink],
  templateUrl: './mascota-adoptable-list.html',
  styles: `
    .adopcion-card { background: white; border-radius: 8px; padding: 20px; box-shadow: 0 2px 4px rgba(0,0,0,0.08); display: flex; flex-direction: column; gap: 8px; }
    .adopcion-card h3 { margin: 0; color: #2e7d32; font-size: 18px; }
    .adopcion-card .label { color: rgba(0,0,0,0.54); font-size: 13px; }
    .adopcion-card .value { font-size: 14px; color: rgba(0,0,0,0.87); }
    .adopcion-card .desc { font-size: 14px; color: rgba(0,0,0,0.7); line-height: 1.5; margin: 4px 0; }
  `
})
export class MascotaAdoptableList implements OnInit {
  private adopcionService = inject(AdopcionService);
  mascotas: MascotaAdoptable[] = [];

  ngOnInit() {
    this.adopcionService.listarDisponibles().subscribe({
      next: (data) => this.mascotas = data,
      error: () => this.mascotas = []
    });
  }
}

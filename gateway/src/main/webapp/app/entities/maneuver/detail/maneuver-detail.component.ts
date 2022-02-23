import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IManeuver } from '../maneuver.model';

@Component({
  selector: 'jhi-maneuver-detail',
  templateUrl: './maneuver-detail.component.html',
})
export class ManeuverDetailComponent implements OnInit {
  maneuver: IManeuver | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ maneuver }) => {
      this.maneuver = maneuver;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

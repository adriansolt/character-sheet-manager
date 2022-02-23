import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { ICampaignUser } from '../campaign-user.model';

@Component({
  selector: 'jhi-campaign-user-detail',
  templateUrl: './campaign-user-detail.component.html',
})
export class CampaignUserDetailComponent implements OnInit {
  campaignUser: ICampaignUser | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ campaignUser }) => {
      this.campaignUser = campaignUser;
    });
  }

  previousState(): void {
    window.history.back();
  }
}

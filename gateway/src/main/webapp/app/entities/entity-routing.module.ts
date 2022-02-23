import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'armor-piece',
        data: { pageTitle: 'gatewayApp.armorPiece.home.title' },
        loadChildren: () => import('./armor-piece/armor-piece.module').then(m => m.ArmorPieceModule),
      },
      {
        path: 'campaign',
        data: { pageTitle: 'gatewayApp.campaign.home.title' },
        loadChildren: () => import('./campaign/campaign.module').then(m => m.CampaignModule),
      },
      {
        path: 'campaign-user',
        data: { pageTitle: 'gatewayApp.campaignUser.home.title' },
        loadChildren: () => import('./campaign-user/campaign-user.module').then(m => m.CampaignUserModule),
      },
      {
        path: 'default-skill-or-atribute',
        data: { pageTitle: 'gatewayApp.defaultSkillOrAtribute.home.title' },
        loadChildren: () =>
          import('./default-skill-or-atribute/default-skill-or-atribute.module').then(m => m.DefaultSkillOrAtributeModule),
      },
      {
        path: 'item',
        data: { pageTitle: 'gatewayApp.item.home.title' },
        loadChildren: () => import('./item/item.module').then(m => m.ItemModule),
      },
      {
        path: 'maneuver',
        data: { pageTitle: 'gatewayApp.maneuver.home.title' },
        loadChildren: () => import('./maneuver/maneuver.module').then(m => m.ManeuverModule),
      },
      {
        path: 'note',
        data: { pageTitle: 'gatewayApp.note.home.title' },
        loadChildren: () => import('./note/note.module').then(m => m.NoteModule),
      },
      {
        path: 'prereq-skill-or-atribute',
        data: { pageTitle: 'gatewayApp.prereqSkillOrAtribute.home.title' },
        loadChildren: () => import('./prereq-skill-or-atribute/prereq-skill-or-atribute.module').then(m => m.PrereqSkillOrAtributeModule),
      },
      {
        path: 'skill',
        data: { pageTitle: 'gatewayApp.skill.home.title' },
        loadChildren: () => import('./skill/skill.module').then(m => m.SkillModule),
      },
      {
        path: 'weapon',
        data: { pageTitle: 'gatewayApp.weapon.home.title' },
        loadChildren: () => import('./weapon/weapon.module').then(m => m.WeaponModule),
      },
      {
        path: 'weapon-maneuver',
        data: { pageTitle: 'gatewayApp.weaponManeuver.home.title' },
        loadChildren: () => import('./weapon-maneuver/weapon-maneuver.module').then(m => m.WeaponManeuverModule),
      },
      {
        path: 'xaracter',
        data: { pageTitle: 'gatewayApp.xaracter.home.title' },
        loadChildren: () => import('./xaracter/xaracter.module').then(m => m.XaracterModule),
      },
      {
        path: 'xaracter-attribute',
        data: { pageTitle: 'gatewayApp.xaracterAttribute.home.title' },
        loadChildren: () => import('./xaracter-attribute/xaracter-attribute.module').then(m => m.XaracterAttributeModule),
      },
      {
        path: 'xaracter-equipped-armor',
        data: { pageTitle: 'gatewayApp.xaracterEquippedArmor.home.title' },
        loadChildren: () => import('./xaracter-equipped-armor/xaracter-equipped-armor.module').then(m => m.XaracterEquippedArmorModule),
      },
      {
        path: 'xaracter-equipped-weapon',
        data: { pageTitle: 'gatewayApp.xaracterEquippedWeapon.home.title' },
        loadChildren: () => import('./xaracter-equipped-weapon/xaracter-equipped-weapon.module').then(m => m.XaracterEquippedWeaponModule),
      },
      {
        path: 'xaracter-skill',
        data: { pageTitle: 'gatewayApp.xaracterSkill.home.title' },
        loadChildren: () => import('./xaracter-skill/xaracter-skill.module').then(m => m.XaracterSkillModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}

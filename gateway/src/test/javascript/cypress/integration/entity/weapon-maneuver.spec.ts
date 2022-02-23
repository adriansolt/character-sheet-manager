import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('WeaponManeuver e2e test', () => {
  const weaponManeuverPageUrl = '/weapon-maneuver';
  const weaponManeuverPageUrlPattern = new RegExp('/weapon-maneuver(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const weaponManeuverSample = {};

  let weaponManeuver: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/weapon-maneuvers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/weapon-maneuvers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/weapon-maneuvers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (weaponManeuver) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/weapon-maneuvers/${weaponManeuver.id}`,
      }).then(() => {
        weaponManeuver = undefined;
      });
    }
  });

  it('WeaponManeuvers menu should load WeaponManeuvers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('weapon-maneuver');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('WeaponManeuver').should('exist');
    cy.url().should('match', weaponManeuverPageUrlPattern);
  });

  describe('WeaponManeuver page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(weaponManeuverPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create WeaponManeuver page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/weapon-maneuver/new$'));
        cy.getEntityCreateUpdateHeading('WeaponManeuver');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponManeuverPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/weapon-maneuvers',
          body: weaponManeuverSample,
        }).then(({ body }) => {
          weaponManeuver = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/weapon-maneuvers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/weapon-maneuvers?page=0&size=20>; rel="last",<http://localhost/api/weapon-maneuvers?page=0&size=20>; rel="first"',
              },
              body: [weaponManeuver],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(weaponManeuverPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details WeaponManeuver page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('weaponManeuver');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponManeuverPageUrlPattern);
      });

      it('edit button click should load edit WeaponManeuver page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('WeaponManeuver');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponManeuverPageUrlPattern);
      });

      it('last delete button click should delete instance of WeaponManeuver', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('weaponManeuver').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', weaponManeuverPageUrlPattern);

        weaponManeuver = undefined;
      });
    });
  });

  describe('new WeaponManeuver page', () => {
    beforeEach(() => {
      cy.visit(`${weaponManeuverPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('WeaponManeuver');
    });

    it('should create an instance of WeaponManeuver', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        weaponManeuver = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', weaponManeuverPageUrlPattern);
    });
  });
});

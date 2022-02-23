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

describe('XaracterEquippedWeapon e2e test', () => {
  const xaracterEquippedWeaponPageUrl = '/xaracter-equipped-weapon';
  const xaracterEquippedWeaponPageUrlPattern = new RegExp('/xaracter-equipped-weapon(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const xaracterEquippedWeaponSample = { xaracterId: 77277 };

  let xaracterEquippedWeapon: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/xaracter-equipped-weapons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/xaracter-equipped-weapons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/xaracter-equipped-weapons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (xaracterEquippedWeapon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/xaracter-equipped-weapons/${xaracterEquippedWeapon.id}`,
      }).then(() => {
        xaracterEquippedWeapon = undefined;
      });
    }
  });

  it('XaracterEquippedWeapons menu should load XaracterEquippedWeapons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('xaracter-equipped-weapon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('XaracterEquippedWeapon').should('exist');
    cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);
  });

  describe('XaracterEquippedWeapon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(xaracterEquippedWeaponPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create XaracterEquippedWeapon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/xaracter-equipped-weapon/new$'));
        cy.getEntityCreateUpdateHeading('XaracterEquippedWeapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/xaracter-equipped-weapons',
          body: xaracterEquippedWeaponSample,
        }).then(({ body }) => {
          xaracterEquippedWeapon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/xaracter-equipped-weapons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/xaracter-equipped-weapons?page=0&size=20>; rel="last",<http://localhost/api/xaracter-equipped-weapons?page=0&size=20>; rel="first"',
              },
              body: [xaracterEquippedWeapon],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(xaracterEquippedWeaponPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details XaracterEquippedWeapon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('xaracterEquippedWeapon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);
      });

      it('edit button click should load edit XaracterEquippedWeapon page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('XaracterEquippedWeapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);
      });

      it('last delete button click should delete instance of XaracterEquippedWeapon', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('xaracterEquippedWeapon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);

        xaracterEquippedWeapon = undefined;
      });
    });
  });

  describe('new XaracterEquippedWeapon page', () => {
    beforeEach(() => {
      cy.visit(`${xaracterEquippedWeaponPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('XaracterEquippedWeapon');
    });

    it('should create an instance of XaracterEquippedWeapon', () => {
      cy.get(`[data-cy="xaracterId"]`).type('69689').should('have.value', '69689');

      cy.get(`[data-cy="hand"]`).select('BOTH');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        xaracterEquippedWeapon = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', xaracterEquippedWeaponPageUrlPattern);
    });
  });
});

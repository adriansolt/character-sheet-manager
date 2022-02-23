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

describe('Maneuver e2e test', () => {
  const maneuverPageUrl = '/maneuver';
  const maneuverPageUrlPattern = new RegExp('/maneuver(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const maneuverSample = { name: 'Chips hierarchy', description: 'Buckinghamshire' };

  let maneuver: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/maneuvers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/maneuvers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/maneuvers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (maneuver) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/maneuvers/${maneuver.id}`,
      }).then(() => {
        maneuver = undefined;
      });
    }
  });

  it('Maneuvers menu should load Maneuvers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('maneuver');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Maneuver').should('exist');
    cy.url().should('match', maneuverPageUrlPattern);
  });

  describe('Maneuver page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(maneuverPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Maneuver page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/maneuver/new$'));
        cy.getEntityCreateUpdateHeading('Maneuver');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', maneuverPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/maneuvers',
          body: maneuverSample,
        }).then(({ body }) => {
          maneuver = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/maneuvers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/maneuvers?page=0&size=20>; rel="last",<http://localhost/api/maneuvers?page=0&size=20>; rel="first"',
              },
              body: [maneuver],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(maneuverPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Maneuver page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('maneuver');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', maneuverPageUrlPattern);
      });

      it('edit button click should load edit Maneuver page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Maneuver');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', maneuverPageUrlPattern);
      });

      it('last delete button click should delete instance of Maneuver', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('maneuver').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', maneuverPageUrlPattern);

        maneuver = undefined;
      });
    });
  });

  describe('new Maneuver page', () => {
    beforeEach(() => {
      cy.visit(`${maneuverPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Maneuver');
    });

    it('should create an instance of Maneuver', () => {
      cy.get(`[data-cy="name"]`).type('Pula').should('have.value', 'Pula');

      cy.get(`[data-cy="modifier"]`).type('23812').should('have.value', '23812');

      cy.get(`[data-cy="description"]`).type('Idaho Gabon').should('have.value', 'Idaho Gabon');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        maneuver = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', maneuverPageUrlPattern);
    });
  });
});

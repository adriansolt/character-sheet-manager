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

describe('XaracterEquippedArmor e2e test', () => {
  const xaracterEquippedArmorPageUrl = '/xaracter-equipped-armor';
  const xaracterEquippedArmorPageUrlPattern = new RegExp('/xaracter-equipped-armor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const xaracterEquippedArmorSample = { xaracterId: 72847 };

  let xaracterEquippedArmor: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/xaracter-equipped-armors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/xaracter-equipped-armors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/xaracter-equipped-armors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (xaracterEquippedArmor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/xaracter-equipped-armors/${xaracterEquippedArmor.id}`,
      }).then(() => {
        xaracterEquippedArmor = undefined;
      });
    }
  });

  it('XaracterEquippedArmors menu should load XaracterEquippedArmors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('xaracter-equipped-armor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('XaracterEquippedArmor').should('exist');
    cy.url().should('match', xaracterEquippedArmorPageUrlPattern);
  });

  describe('XaracterEquippedArmor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(xaracterEquippedArmorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create XaracterEquippedArmor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/xaracter-equipped-armor/new$'));
        cy.getEntityCreateUpdateHeading('XaracterEquippedArmor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedArmorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/xaracter-equipped-armors',
          body: xaracterEquippedArmorSample,
        }).then(({ body }) => {
          xaracterEquippedArmor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/xaracter-equipped-armors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/xaracter-equipped-armors?page=0&size=20>; rel="last",<http://localhost/api/xaracter-equipped-armors?page=0&size=20>; rel="first"',
              },
              body: [xaracterEquippedArmor],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(xaracterEquippedArmorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details XaracterEquippedArmor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('xaracterEquippedArmor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedArmorPageUrlPattern);
      });

      it('edit button click should load edit XaracterEquippedArmor page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('XaracterEquippedArmor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedArmorPageUrlPattern);
      });

      it('last delete button click should delete instance of XaracterEquippedArmor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('xaracterEquippedArmor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterEquippedArmorPageUrlPattern);

        xaracterEquippedArmor = undefined;
      });
    });
  });

  describe('new XaracterEquippedArmor page', () => {
    beforeEach(() => {
      cy.visit(`${xaracterEquippedArmorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('XaracterEquippedArmor');
    });

    it('should create an instance of XaracterEquippedArmor', () => {
      cy.get(`[data-cy="xaracterId"]`).type('88998').should('have.value', '88998');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        xaracterEquippedArmor = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', xaracterEquippedArmorPageUrlPattern);
    });
  });
});

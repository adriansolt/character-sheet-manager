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

describe('XaracterAttribute e2e test', () => {
  const xaracterAttributePageUrl = '/xaracter-attribute';
  const xaracterAttributePageUrlPattern = new RegExp('/xaracter-attribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const xaracterAttributeSample = { name: 'IQ', points: 31164 };

  let xaracterAttribute: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/xaracter-attributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/xaracter-attributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/xaracter-attributes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (xaracterAttribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/xaracter-attributes/${xaracterAttribute.id}`,
      }).then(() => {
        xaracterAttribute = undefined;
      });
    }
  });

  it('XaracterAttributes menu should load XaracterAttributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('xaracter-attribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('XaracterAttribute').should('exist');
    cy.url().should('match', xaracterAttributePageUrlPattern);
  });

  describe('XaracterAttribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(xaracterAttributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create XaracterAttribute page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/xaracter-attribute/new$'));
        cy.getEntityCreateUpdateHeading('XaracterAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterAttributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/xaracter-attributes',
          body: xaracterAttributeSample,
        }).then(({ body }) => {
          xaracterAttribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/xaracter-attributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/xaracter-attributes?page=0&size=20>; rel="last",<http://localhost/api/xaracter-attributes?page=0&size=20>; rel="first"',
              },
              body: [xaracterAttribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(xaracterAttributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details XaracterAttribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('xaracterAttribute');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterAttributePageUrlPattern);
      });

      it('edit button click should load edit XaracterAttribute page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('XaracterAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterAttributePageUrlPattern);
      });

      it('last delete button click should delete instance of XaracterAttribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('xaracterAttribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterAttributePageUrlPattern);

        xaracterAttribute = undefined;
      });
    });
  });

  describe('new XaracterAttribute page', () => {
    beforeEach(() => {
      cy.visit(`${xaracterAttributePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('XaracterAttribute');
    });

    it('should create an instance of XaracterAttribute', () => {
      cy.get(`[data-cy="name"]`).select('HT');

      cy.get(`[data-cy="points"]`).type('63004').should('have.value', '63004');

      cy.get(`[data-cy="attributeModifier"]`).type('55459').should('have.value', '55459');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        xaracterAttribute = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', xaracterAttributePageUrlPattern);
    });
  });
});

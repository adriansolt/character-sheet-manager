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

describe('ArmorPiece e2e test', () => {
  const armorPiecePageUrl = '/armor-piece';
  const armorPiecePageUrlPattern = new RegExp('/armor-piece(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const armorPieceSample = { name: 'connecting generating neural', weight: 41122, quality: 33610 };

  let armorPiece: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/armor-pieces+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/armor-pieces').as('postEntityRequest');
    cy.intercept('DELETE', '/api/armor-pieces/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (armorPiece) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/armor-pieces/${armorPiece.id}`,
      }).then(() => {
        armorPiece = undefined;
      });
    }
  });

  it('ArmorPieces menu should load ArmorPieces page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('armor-piece');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ArmorPiece').should('exist');
    cy.url().should('match', armorPiecePageUrlPattern);
  });

  describe('ArmorPiece page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(armorPiecePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ArmorPiece page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/armor-piece/new$'));
        cy.getEntityCreateUpdateHeading('ArmorPiece');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', armorPiecePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/armor-pieces',
          body: armorPieceSample,
        }).then(({ body }) => {
          armorPiece = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/armor-pieces+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/armor-pieces?page=0&size=20>; rel="last",<http://localhost/api/armor-pieces?page=0&size=20>; rel="first"',
              },
              body: [armorPiece],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(armorPiecePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ArmorPiece page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('armorPiece');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', armorPiecePageUrlPattern);
      });

      it('edit button click should load edit ArmorPiece page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ArmorPiece');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', armorPiecePageUrlPattern);
      });

      it('last delete button click should delete instance of ArmorPiece', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('armorPiece').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', armorPiecePageUrlPattern);

        armorPiece = undefined;
      });
    });
  });

  describe('new ArmorPiece page', () => {
    beforeEach(() => {
      cy.visit(`${armorPiecePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ArmorPiece');
    });

    it('should create an instance of ArmorPiece', () => {
      cy.get(`[data-cy="name"]`).type('Assurance Monaco').should('have.value', 'Assurance Monaco');

      cy.get(`[data-cy="description"]`).type('Branding port Designer').should('have.value', 'Branding port Designer');

      cy.get(`[data-cy="weight"]`).type('21558').should('have.value', '21558');

      cy.get(`[data-cy="quality"]`).type('94197').should('have.value', '94197');

      cy.setFieldImageAsBytesOfEntity('picture', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="location"]`).select('LEFT_ARM');

      cy.get(`[data-cy="defenseModifier"]`).type('97084').should('have.value', '97084');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        armorPiece = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', armorPiecePageUrlPattern);
    });
  });
});

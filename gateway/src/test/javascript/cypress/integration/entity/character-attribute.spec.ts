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

describe('CharacterAttribute e2e test', () => {
  const characterAttributePageUrl = '/character-attribute';
  const characterAttributePageUrlPattern = new RegExp('/character-attribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const characterAttributeSample = { name: 'ST', points: 99554 };

  let characterAttribute: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/character-attributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/character-attributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/character-attributes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (characterAttribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/character-attributes/${characterAttribute.id}`,
      }).then(() => {
        characterAttribute = undefined;
      });
    }
  });

  it('CharacterAttributes menu should load CharacterAttributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('character-attribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CharacterAttribute').should('exist');
    cy.url().should('match', characterAttributePageUrlPattern);
  });

  describe('CharacterAttribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(characterAttributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CharacterAttribute page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/character-attribute/new$'));
        cy.getEntityCreateUpdateHeading('CharacterAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterAttributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/character-attributes',
          body: characterAttributeSample,
        }).then(({ body }) => {
          characterAttribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/character-attributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/character-attributes?page=0&size=20>; rel="last",<http://localhost/api/character-attributes?page=0&size=20>; rel="first"',
              },
              body: [characterAttribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(characterAttributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CharacterAttribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('characterAttribute');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterAttributePageUrlPattern);
      });

      it('edit button click should load edit CharacterAttribute page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CharacterAttribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterAttributePageUrlPattern);
      });

      it('last delete button click should delete instance of CharacterAttribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('characterAttribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterAttributePageUrlPattern);

        characterAttribute = undefined;
      });
    });
  });

  describe('new CharacterAttribute page', () => {
    beforeEach(() => {
      cy.visit(`${characterAttributePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CharacterAttribute');
    });

    it('should create an instance of CharacterAttribute', () => {
      cy.get(`[data-cy="name"]`).select('IQ');

      cy.get(`[data-cy="points"]`).type('79531').should('have.value', '79531');

      cy.get(`[data-cy="attributeModifier"]`).type('14259').should('have.value', '14259');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        characterAttribute = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', characterAttributePageUrlPattern);
    });
  });
});

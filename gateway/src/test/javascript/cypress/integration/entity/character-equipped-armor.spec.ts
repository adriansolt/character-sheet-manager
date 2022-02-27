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

describe('CharacterEquippedArmor e2e test', () => {
  const characterEquippedArmorPageUrl = '/character-equipped-armor';
  const characterEquippedArmorPageUrlPattern = new RegExp('/character-equipped-armor(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const characterEquippedArmorSample = {};

  let characterEquippedArmor: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/character-equipped-armors+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/character-equipped-armors').as('postEntityRequest');
    cy.intercept('DELETE', '/api/character-equipped-armors/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (characterEquippedArmor) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/character-equipped-armors/${characterEquippedArmor.id}`,
      }).then(() => {
        characterEquippedArmor = undefined;
      });
    }
  });

  it('CharacterEquippedArmors menu should load CharacterEquippedArmors page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('character-equipped-armor');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CharacterEquippedArmor').should('exist');
    cy.url().should('match', characterEquippedArmorPageUrlPattern);
  });

  describe('CharacterEquippedArmor page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(characterEquippedArmorPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CharacterEquippedArmor page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/character-equipped-armor/new$'));
        cy.getEntityCreateUpdateHeading('CharacterEquippedArmor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedArmorPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/character-equipped-armors',
          body: characterEquippedArmorSample,
        }).then(({ body }) => {
          characterEquippedArmor = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/character-equipped-armors+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/character-equipped-armors?page=0&size=20>; rel="last",<http://localhost/api/character-equipped-armors?page=0&size=20>; rel="first"',
              },
              body: [characterEquippedArmor],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(characterEquippedArmorPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CharacterEquippedArmor page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('characterEquippedArmor');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedArmorPageUrlPattern);
      });

      it('edit button click should load edit CharacterEquippedArmor page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CharacterEquippedArmor');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedArmorPageUrlPattern);
      });

      it('last delete button click should delete instance of CharacterEquippedArmor', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('characterEquippedArmor').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedArmorPageUrlPattern);

        characterEquippedArmor = undefined;
      });
    });
  });

  describe('new CharacterEquippedArmor page', () => {
    beforeEach(() => {
      cy.visit(`${characterEquippedArmorPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CharacterEquippedArmor');
    });

    it('should create an instance of CharacterEquippedArmor', () => {
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        characterEquippedArmor = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', characterEquippedArmorPageUrlPattern);
    });
  });
});

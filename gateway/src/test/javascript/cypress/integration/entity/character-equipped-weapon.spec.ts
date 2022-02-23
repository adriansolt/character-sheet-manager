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

describe('CharacterEquippedWeapon e2e test', () => {
  const characterEquippedWeaponPageUrl = '/character-equipped-weapon';
  const characterEquippedWeaponPageUrlPattern = new RegExp('/character-equipped-weapon(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const characterEquippedWeaponSample = { characterId: 766 };

  let characterEquippedWeapon: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/character-equipped-weapons+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/character-equipped-weapons').as('postEntityRequest');
    cy.intercept('DELETE', '/api/character-equipped-weapons/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (characterEquippedWeapon) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/character-equipped-weapons/${characterEquippedWeapon.id}`,
      }).then(() => {
        characterEquippedWeapon = undefined;
      });
    }
  });

  it('CharacterEquippedWeapons menu should load CharacterEquippedWeapons page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('character-equipped-weapon');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CharacterEquippedWeapon').should('exist');
    cy.url().should('match', characterEquippedWeaponPageUrlPattern);
  });

  describe('CharacterEquippedWeapon page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(characterEquippedWeaponPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CharacterEquippedWeapon page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/character-equipped-weapon/new$'));
        cy.getEntityCreateUpdateHeading('CharacterEquippedWeapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedWeaponPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/character-equipped-weapons',
          body: characterEquippedWeaponSample,
        }).then(({ body }) => {
          characterEquippedWeapon = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/character-equipped-weapons+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/character-equipped-weapons?page=0&size=20>; rel="last",<http://localhost/api/character-equipped-weapons?page=0&size=20>; rel="first"',
              },
              body: [characterEquippedWeapon],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(characterEquippedWeaponPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CharacterEquippedWeapon page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('characterEquippedWeapon');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedWeaponPageUrlPattern);
      });

      it('edit button click should load edit CharacterEquippedWeapon page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CharacterEquippedWeapon');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedWeaponPageUrlPattern);
      });

      it('last delete button click should delete instance of CharacterEquippedWeapon', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('characterEquippedWeapon').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterEquippedWeaponPageUrlPattern);

        characterEquippedWeapon = undefined;
      });
    });
  });

  describe('new CharacterEquippedWeapon page', () => {
    beforeEach(() => {
      cy.visit(`${characterEquippedWeaponPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CharacterEquippedWeapon');
    });

    it('should create an instance of CharacterEquippedWeapon', () => {
      cy.get(`[data-cy="characterId"]`).type('76915').should('have.value', '76915');

      cy.get(`[data-cy="hand"]`).select('LEFT');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        characterEquippedWeapon = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', characterEquippedWeaponPageUrlPattern);
    });
  });
});

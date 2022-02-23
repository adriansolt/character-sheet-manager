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

describe('CharacterSkill e2e test', () => {
  const characterSkillPageUrl = '/character-skill';
  const characterSkillPageUrlPattern = new RegExp('/character-skill(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const characterSkillSample = { points: 58542 };

  let characterSkill: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/character-skills+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/character-skills').as('postEntityRequest');
    cy.intercept('DELETE', '/api/character-skills/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (characterSkill) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/character-skills/${characterSkill.id}`,
      }).then(() => {
        characterSkill = undefined;
      });
    }
  });

  it('CharacterSkills menu should load CharacterSkills page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('character-skill');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CharacterSkill').should('exist');
    cy.url().should('match', characterSkillPageUrlPattern);
  });

  describe('CharacterSkill page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(characterSkillPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CharacterSkill page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/character-skill/new$'));
        cy.getEntityCreateUpdateHeading('CharacterSkill');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterSkillPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/character-skills',
          body: characterSkillSample,
        }).then(({ body }) => {
          characterSkill = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/character-skills+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/character-skills?page=0&size=20>; rel="last",<http://localhost/api/character-skills?page=0&size=20>; rel="first"',
              },
              body: [characterSkill],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(characterSkillPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details CharacterSkill page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('characterSkill');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterSkillPageUrlPattern);
      });

      it('edit button click should load edit CharacterSkill page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CharacterSkill');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterSkillPageUrlPattern);
      });

      it('last delete button click should delete instance of CharacterSkill', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('characterSkill').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterSkillPageUrlPattern);

        characterSkill = undefined;
      });
    });
  });

  describe('new CharacterSkill page', () => {
    beforeEach(() => {
      cy.visit(`${characterSkillPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CharacterSkill');
    });

    it('should create an instance of CharacterSkill', () => {
      cy.get(`[data-cy="points"]`).type('43324').should('have.value', '43324');

      cy.get(`[data-cy="skillModifier"]`).type('46790').should('have.value', '46790');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        characterSkill = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', characterSkillPageUrlPattern);
    });
  });
});

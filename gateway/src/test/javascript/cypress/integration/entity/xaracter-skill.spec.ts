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

describe('XaracterSkill e2e test', () => {
  const xaracterSkillPageUrl = '/xaracter-skill';
  const xaracterSkillPageUrlPattern = new RegExp('/xaracter-skill(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const xaracterSkillSample = { points: 53482 };

  let xaracterSkill: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/xaracter-skills+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/xaracter-skills').as('postEntityRequest');
    cy.intercept('DELETE', '/api/xaracter-skills/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (xaracterSkill) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/xaracter-skills/${xaracterSkill.id}`,
      }).then(() => {
        xaracterSkill = undefined;
      });
    }
  });

  it('XaracterSkills menu should load XaracterSkills page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('xaracter-skill');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('XaracterSkill').should('exist');
    cy.url().should('match', xaracterSkillPageUrlPattern);
  });

  describe('XaracterSkill page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(xaracterSkillPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create XaracterSkill page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/xaracter-skill/new$'));
        cy.getEntityCreateUpdateHeading('XaracterSkill');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterSkillPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/xaracter-skills',
          body: xaracterSkillSample,
        }).then(({ body }) => {
          xaracterSkill = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/xaracter-skills+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/xaracter-skills?page=0&size=20>; rel="last",<http://localhost/api/xaracter-skills?page=0&size=20>; rel="first"',
              },
              body: [xaracterSkill],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(xaracterSkillPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details XaracterSkill page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('xaracterSkill');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterSkillPageUrlPattern);
      });

      it('edit button click should load edit XaracterSkill page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('XaracterSkill');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterSkillPageUrlPattern);
      });

      it('last delete button click should delete instance of XaracterSkill', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('xaracterSkill').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', xaracterSkillPageUrlPattern);

        xaracterSkill = undefined;
      });
    });
  });

  describe('new XaracterSkill page', () => {
    beforeEach(() => {
      cy.visit(`${xaracterSkillPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('XaracterSkill');
    });

    it('should create an instance of XaracterSkill', () => {
      cy.get(`[data-cy="points"]`).type('61613').should('have.value', '61613');

      cy.get(`[data-cy="skillModifier"]`).type('21518').should('have.value', '21518');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        xaracterSkill = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', xaracterSkillPageUrlPattern);
    });
  });
});

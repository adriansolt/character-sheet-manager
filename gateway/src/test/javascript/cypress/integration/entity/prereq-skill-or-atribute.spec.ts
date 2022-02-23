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

describe('PrereqSkillOrAtribute e2e test', () => {
  const prereqSkillOrAtributePageUrl = '/prereq-skill-or-atribute';
  const prereqSkillOrAtributePageUrlPattern = new RegExp('/prereq-skill-or-atribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const prereqSkillOrAtributeSample = { name: 'Fantastic', level: 38796 };

  let prereqSkillOrAtribute: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/prereq-skill-or-atributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/prereq-skill-or-atributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/prereq-skill-or-atributes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (prereqSkillOrAtribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/prereq-skill-or-atributes/${prereqSkillOrAtribute.id}`,
      }).then(() => {
        prereqSkillOrAtribute = undefined;
      });
    }
  });

  it('PrereqSkillOrAtributes menu should load PrereqSkillOrAtributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('prereq-skill-or-atribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PrereqSkillOrAtribute').should('exist');
    cy.url().should('match', prereqSkillOrAtributePageUrlPattern);
  });

  describe('PrereqSkillOrAtribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(prereqSkillOrAtributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PrereqSkillOrAtribute page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/prereq-skill-or-atribute/new$'));
        cy.getEntityCreateUpdateHeading('PrereqSkillOrAtribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prereqSkillOrAtributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/prereq-skill-or-atributes',
          body: prereqSkillOrAtributeSample,
        }).then(({ body }) => {
          prereqSkillOrAtribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/prereq-skill-or-atributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/prereq-skill-or-atributes?page=0&size=20>; rel="last",<http://localhost/api/prereq-skill-or-atributes?page=0&size=20>; rel="first"',
              },
              body: [prereqSkillOrAtribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(prereqSkillOrAtributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PrereqSkillOrAtribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('prereqSkillOrAtribute');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prereqSkillOrAtributePageUrlPattern);
      });

      it('edit button click should load edit PrereqSkillOrAtribute page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PrereqSkillOrAtribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prereqSkillOrAtributePageUrlPattern);
      });

      it('last delete button click should delete instance of PrereqSkillOrAtribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('prereqSkillOrAtribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', prereqSkillOrAtributePageUrlPattern);

        prereqSkillOrAtribute = undefined;
      });
    });
  });

  describe('new PrereqSkillOrAtribute page', () => {
    beforeEach(() => {
      cy.visit(`${prereqSkillOrAtributePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PrereqSkillOrAtribute');
    });

    it('should create an instance of PrereqSkillOrAtribute', () => {
      cy.get(`[data-cy="name"]`).type('magenta Krone').should('have.value', 'magenta Krone');

      cy.get(`[data-cy="level"]`).type('4130').should('have.value', '4130');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        prereqSkillOrAtribute = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', prereqSkillOrAtributePageUrlPattern);
    });
  });
});

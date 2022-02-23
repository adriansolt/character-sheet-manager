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

describe('DefaultSkillOrAtribute e2e test', () => {
  const defaultSkillOrAtributePageUrl = '/default-skill-or-atribute';
  const defaultSkillOrAtributePageUrlPattern = new RegExp('/default-skill-or-atribute(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const defaultSkillOrAtributeSample = { name: 'Salad Oklahoma', modifier: 92172 };

  let defaultSkillOrAtribute: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/default-skill-or-atributes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/default-skill-or-atributes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/default-skill-or-atributes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (defaultSkillOrAtribute) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/default-skill-or-atributes/${defaultSkillOrAtribute.id}`,
      }).then(() => {
        defaultSkillOrAtribute = undefined;
      });
    }
  });

  it('DefaultSkillOrAtributes menu should load DefaultSkillOrAtributes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('default-skill-or-atribute');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('DefaultSkillOrAtribute').should('exist');
    cy.url().should('match', defaultSkillOrAtributePageUrlPattern);
  });

  describe('DefaultSkillOrAtribute page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(defaultSkillOrAtributePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create DefaultSkillOrAtribute page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/default-skill-or-atribute/new$'));
        cy.getEntityCreateUpdateHeading('DefaultSkillOrAtribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', defaultSkillOrAtributePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/default-skill-or-atributes',
          body: defaultSkillOrAtributeSample,
        }).then(({ body }) => {
          defaultSkillOrAtribute = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/default-skill-or-atributes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/default-skill-or-atributes?page=0&size=20>; rel="last",<http://localhost/api/default-skill-or-atributes?page=0&size=20>; rel="first"',
              },
              body: [defaultSkillOrAtribute],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(defaultSkillOrAtributePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details DefaultSkillOrAtribute page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('defaultSkillOrAtribute');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', defaultSkillOrAtributePageUrlPattern);
      });

      it('edit button click should load edit DefaultSkillOrAtribute page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('DefaultSkillOrAtribute');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', defaultSkillOrAtributePageUrlPattern);
      });

      it('last delete button click should delete instance of DefaultSkillOrAtribute', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('defaultSkillOrAtribute').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', defaultSkillOrAtributePageUrlPattern);

        defaultSkillOrAtribute = undefined;
      });
    });
  });

  describe('new DefaultSkillOrAtribute page', () => {
    beforeEach(() => {
      cy.visit(`${defaultSkillOrAtributePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('DefaultSkillOrAtribute');
    });

    it('should create an instance of DefaultSkillOrAtribute', () => {
      cy.get(`[data-cy="name"]`).type('Nepal Plastic').should('have.value', 'Nepal Plastic');

      cy.get(`[data-cy="modifier"]`).type('4829').should('have.value', '4829');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        defaultSkillOrAtribute = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', defaultSkillOrAtributePageUrlPattern);
    });
  });
});

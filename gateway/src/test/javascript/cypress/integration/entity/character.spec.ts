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

describe('Character e2e test', () => {
  const characterPageUrl = '/character';
  const characterPageUrlPattern = new RegExp('/character(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const characterSample = { name: 'indigo', weight: 47228, height: 22892, points: 55951, handedness: 'BOTH', active: true };

  let character: any;
  //let user: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"id":"d07aa1d1-57e3-41c9-9e61-ff6f23bdc324","login":"Automotive Compatible","firstName":"America","lastName":"Raynor"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/characters+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/characters').as('postEntityRequest');
    cy.intercept('DELETE', '/api/characters/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/items', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/weapons', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/armor-pieces', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/notes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/character-attributes', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/character-skills', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

    cy.intercept('GET', '/api/campaigns', {
      statusCode: 200,
      body: [],
    });

  });
   */

  afterEach(() => {
    if (character) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/characters/${character.id}`,
      }).then(() => {
        character = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('Characters menu should load Characters page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('character');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Character').should('exist');
    cy.url().should('match', characterPageUrlPattern);
  });

  describe('Character page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(characterPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Character page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/character/new$'));
        cy.getEntityCreateUpdateHeading('Character');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/characters',
          body: {
            ...characterSample,
            user: user,
          },
        }).then(({ body }) => {
          character = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/characters+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/characters?page=0&size=20>; rel="last",<http://localhost/api/characters?page=0&size=20>; rel="first"',
              },
              body: [character],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(characterPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(characterPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Character page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('character');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterPageUrlPattern);
      });

      it('edit button click should load edit Character page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Character');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Character', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('character').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', characterPageUrlPattern);

        character = undefined;
      });
    });
  });

  describe('new Character page', () => {
    beforeEach(() => {
      cy.visit(`${characterPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Character');
    });

    it.skip('should create an instance of Character', () => {
      cy.get(`[data-cy="name"]`).type('Lead Account Circle').should('have.value', 'Lead Account Circle');

      cy.get(`[data-cy="weight"]`).type('95160').should('have.value', '95160');

      cy.get(`[data-cy="height"]`).type('13599').should('have.value', '13599');

      cy.get(`[data-cy="points"]`).type('52664').should('have.value', '52664');

      cy.setFieldImageAsBytesOfEntity('picture', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="handedness"]`).select('BOTH');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click().should('be.checked');

      cy.get(`[data-cy="user"]`).select(1);

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        character = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', characterPageUrlPattern);
    });
  });
});

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

describe('CampaignUser e2e test', () => {
  const campaignUserPageUrl = '/campaign-user';
  const campaignUserPageUrlPattern = new RegExp('/campaign-user(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const campaignUserSample = {};

  let campaignUser: any;
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
      body: {"id":"cb202a4b-d402-4f15-b38f-bcb5783e88aa","login":"Human Creative redundant","firstName":"Cornell","lastName":"Muller"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/campaign-users+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/campaign-users').as('postEntityRequest');
    cy.intercept('DELETE', '/api/campaign-users/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/campaigns', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (campaignUser) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/campaign-users/${campaignUser.id}`,
      }).then(() => {
        campaignUser = undefined;
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

  it('CampaignUsers menu should load CampaignUsers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('campaign-user');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('CampaignUser').should('exist');
    cy.url().should('match', campaignUserPageUrlPattern);
  });

  describe('CampaignUser page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(campaignUserPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create CampaignUser page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/campaign-user/new$'));
        cy.getEntityCreateUpdateHeading('CampaignUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignUserPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/campaign-users',
          body: {
            ...campaignUserSample,
            user: user,
          },
        }).then(({ body }) => {
          campaignUser = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/campaign-users+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/campaign-users?page=0&size=20>; rel="last",<http://localhost/api/campaign-users?page=0&size=20>; rel="first"',
              },
              body: [campaignUser],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(campaignUserPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(campaignUserPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response!.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details CampaignUser page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('campaignUser');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignUserPageUrlPattern);
      });

      it('edit button click should load edit CampaignUser page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('CampaignUser');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignUserPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of CampaignUser', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('campaignUser').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', campaignUserPageUrlPattern);

        campaignUser = undefined;
      });
    });
  });

  describe('new CampaignUser page', () => {
    beforeEach(() => {
      cy.visit(`${campaignUserPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('CampaignUser');
    });

    it.skip('should create an instance of CampaignUser', () => {
      cy.get(`[data-cy="user"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        campaignUser = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', campaignUserPageUrlPattern);
    });
  });
});

package it.polimi.ingsw.model.usersdb;


import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotFindUserInDBException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotLoginUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.CannotRegisterUserException;
import it.polimi.ingsw.model.exceptions.usersAndDatabaseExceptions.NullTokenException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.mockito.Mockito.mock;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)

class DatabaseUsersTest {
    static private DatabaseUsers db1, db2, db3, db4;
    static String path = "src/test/resources/databasetest/database.db";
    static File f;


    @BeforeAll
    static void setUpClass() {
        f = new File(path);
        if (f.isFile())
            f.delete();
        db1 = DatabaseUsers.getInstance(path);
        db2 = DatabaseUsers.getInstance(path);
        db3 = DatabaseUsers.getInstance(path);
        db4 = DatabaseUsers.getInstance(path);

    }

    @Test
    public void getInstanceTest() {
        Assertions.assertEquals(db1, db2);
        Assertions.assertEquals(db2, db3);
        Assertions.assertEquals(db3, db4);
    }

    @Test
    public void registerUserTest() {
        Assertions.assertDoesNotThrow(() -> {
            db1.registerUser("baffo", "12345");
        });
    }

    @Test
    public void registerTheSameUser() {
        db1.registerUser("affo", "12345");
        Assertions.assertThrows(CannotRegisterUserException.class, () -> {
            db1.registerUser("affo", "12345");
        });
    }



    @Nested @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class neededDBStoredData {



        @Test
        public void loginRegisteredUser() {
            Assertions.assertDoesNotThrow(() -> {
                db1.login("affo", "12345");
            });
        }

        @Test
        void loginWrongPasswordUser() {
            Assertions.assertThrows(CannotLoginUserException.class, () -> {
                db1.login("affo", "wrongPassword");
            });

        }


        @Test
        void loginWrongUser() {
            Assertions.assertThrows(CannotLoginUserException.class, () -> {
                db1.login("bababa", "12345");
            });
        }


        @Test
        void differentTokensForDifferentUsers() {
            Assertions.assertNotEquals(db1.login("affo", "12345"), db1.login("baffo", "12345"));
        }

        @Test
        void differentTokensSameUserMoreLogins() {
            Assertions.assertNotEquals(db1.login("affo", "12345"), db1.login("affo", "12345"));
        }

        @Test
        void getWonGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getWonGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getWonGamesFromNullToken() {
            Assertions.assertThrows(NullTokenException.class, () -> {
                db1.getWonGamesFromToken(null);
            });
        }


        @Test
        void getLostGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getLostGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getLostGamesFromNullToken() {
            Assertions.assertThrows(NullTokenException.class, () -> {
                db1.getLostGamesFromToken(null);
            });
        }


        @Test
        void getAbandonedGamesFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getAbandonedGamesFromToken("WrongToken123");
            });
        }

        @Test
        void getAbandonedGamesFromNullToken() {
            Assertions.assertThrows(NullTokenException.class, () -> {
                db1.getAbandonedGamesFromToken(null);
            });
        }


        @Test
        void getRankingFromWrongToken() {
            Assertions.assertThrows(CannotFindUserInDBException.class, () -> {
                db1.getRankingFromToken("WrongToken123");
            });
        }

        @Test
        void getRankingFromNullToken() {
            Assertions.assertThrows(NullTokenException.class, () -> {
                db1.getRankingFromToken(null);
            });
        }


        @Nested @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class neededDbToken {
            String token = db1.login("affo", "12345");


            @Test
            void getRankingFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getRankingFromToken(token);
                });
            }

            @Test
            void getWonGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getWonGamesFromToken(token);
                });
            }

            @Test
            void getAbandonedGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getAbandonedGamesFromToken(token);
                });
            }

            @Test
            void getLostGamesFromRightToken() {
                Assertions.assertDoesNotThrow(() -> {
                    db1.getLostGamesFromToken(token);
                });
            }
        }


        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        class TestFromUsername{
            @BeforeAll
            void setUpNewUser(){
                db1.registerUser("ilario","test");

            }


            @Test
            void getWonGamesFromUsernameTest(){
                Assertions.assertEquals(0,db1.getWonGamesFromUsername("ilario"));
            }

            @Test
            void addWonGamesFromUsernameTest(){
                db1.addWonGamesFromUsername("ilario");
                Assertions.assertEquals(1,db1.getWonGamesFromUsername("ilario"));
            }


            @Test
            void getLostGamesFromUsernameTest(){
                Assertions.assertEquals(0,db1.getLostGamesFromUsername("ilario"));
            }

            @Test
            void addLostGamesFromUsernameTest(){
                db1.addLostGamesFromUsername("ilario");
                Assertions.assertEquals(1,db1.getLostGamesFromUsername("ilario"));
            }


            @Test
            void getAbandonedGamesFromUsernameTest(){
                Assertions.assertEquals(0,db1.getAbandonedGamesFromUsername("ilario"));
            }

            @Test
            void addAbandonedGamesFromUsernameTest(){
                db1.addAbandonedGamesFromUsername("ilario");
                Assertions.assertEquals(1,db1.getAbandonedGamesFromUsername("ilario"));
            }


            @Test
            void getRankingFromUsernameTest(){
                Assertions.assertEquals(0,db1.getRankingFromUsername("ilario"));
            }

            @Test
            void addPointsToRankingFromUsernameTest(){
                db1.addPointsRankingFromUsername("ilario",12);
                Assertions.assertEquals(12,db1.getRankingFromUsername("ilario"));
            }


        }


    }



    @AfterAll
    static void removeFile() {
        f= new File(path);
        if (f != null) {
             f.delete();
        }
    }
}



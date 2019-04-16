package te.groovy.examples.gia2.Ch2_Basics

class Profile {
    String street = "some street"
    boolean enabled = true
    List<String> roles = ["ROLE_USER"]
}

Profile profile = new Profile()

assert profile.street == profile.getStreet()
assert profile.enabled == profile.isEnabled()
assert profile.roles == profile.getRoles()